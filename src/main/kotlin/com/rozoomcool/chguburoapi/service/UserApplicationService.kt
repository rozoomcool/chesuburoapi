package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.entity.ApplicationStatus
import com.rozoomcool.chguburoapi.entity.Document
import com.rozoomcool.chguburoapi.entity.User
import com.rozoomcool.chguburoapi.entity.UserApplication
import com.rozoomcool.chguburoapi.exception.EntityNotFoundException
import com.rozoomcool.chguburoapi.repository.ServiceDataRepository
import com.rozoomcool.chguburoapi.repository.UserApplicationRepository
import com.rozoomcool.chguburoapi.repository.UserRepository
import com.rozoomcool.chguburoapi.util.storage.FileSystemStorageService
import jakarta.transaction.Transactional
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class UserApplicationService(
    private val userApplicationRepository: UserApplicationRepository,
    private val userRepository: UserRepository,
    private val serviceDataRepository: ServiceDataRepository,
    private val fileSystemStorageService: FileSystemStorageService,
    @Value("\${dirs.uploads}")
    val uploadsDir: String,
) {

    @Transactional
    fun createApplication(username: String, serviceId: Long): UserApplication {
        val currentUser =
            userRepository.findByUsername(username).getOrNull() ?: throw UsernameNotFoundException("User not found")
        val serviceData =
            serviceDataRepository.findById(serviceId).getOrNull() ?: throw EntityNotFoundException("Service not found")

        val reference = fileSystemStorageService.load(serviceData.document?.filename ?: "mestotrebdoc.docx")
        val image = fileSystemStorageService.load(serviceData.document?.filename ?: "podpis.png")
        println(":::: ref $reference")
        val filename = createDocumentWithUserData(reference,  image, currentUser)
        println(":::: filename $filename")

        return userApplicationRepository.save(
            UserApplication(
                user = currentUser,
                service = serviceData,
                document = Document(filename = filename),
                applicationStatus = ApplicationStatus.SENT
            )
        )
    }

    private fun createDocumentWithUserData(templatePath: Path, imagePath: Path, user: User): String {
        val currentDate = LocalDate.now()

        // 1. Открываем шаблон документа
        Files.newInputStream(templatePath).use { inputStream ->
            val document = XWPFDocument(inputStream)

            // 2. Проходим по абзацам вне таблиц и заменяем маркеры
            document.paragraphs.forEach { paragraph ->
                paragraph.runs.forEach { run ->
                    var runText = run.text()

                    // Преобразуем текст с заменой маркеров
                    val updatedText = runText
                        .replace("{{fullname}}", user.profile?.passportData?.fullname ?: "")
                        .replace("{{userCourse}}", "4")
                        .replace("{{day}}", currentDate.dayOfMonth.toString())
                        .replace("{{month}}", currentDate.monthValue.toString())
                        .replace("{{year}}", currentDate.year.toString())

                    // Удаляем старый текст и устанавливаем новый
                    run.setText("", 0) // Удаляем старый текст
                    run.setText(updatedText, 0) // Устанавливаем новый текст
                }
            }

            // 3. Проходим по таблицам и заменяем маркеры внутри ячеек таблицы
            document.tables.forEach { table ->
                table.rows.forEach { row ->
                    row.tableCells.forEach { cell ->
                        cell.paragraphs.forEach { paragraph ->
                            paragraph.runs.forEach { run ->
                                var runText = run.text()

                                // Преобразуем текст с заменой маркеров
                                val updatedText = runText
                                    .replace("{{fullname}}", user.profile?.passportData?.fullname ?: "")
                                    .replace("{{userCourse}}", "4")
                                    .replace("{{day}}", currentDate.dayOfMonth.toString())
                                    .replace("{{month}}", currentDate.monthValue.toString())
                                    .replace("{{year}}", currentDate.year.toString())

                                // Удаляем старый текст и устанавливаем новый
                                run.setText("", 0) // Удаляем старый текст
                                run.setText(updatedText, 0) // Устанавливаем новый текст
                            }
                        }
                    }
                }
            }

//            // 4. Вставляем изображение в конец документа
//            if (Files.exists(imagePath)) {
//                val imageData = Files.readAllBytes(imagePath)
//                val pictureType = getPictureType(imagePath)
//
//                // Создаем новый абзац для изображения
//                val imageParagraph = document.createParagraph()
//                val run = imageParagraph.createRun()
//
//                // Вставляем изображение
//                val width = 150 // ширина изображения в пикселях
//                val height = 150 // высота изображения в пикселях
//                Files.newInputStream(imagePath).use { imageInputStream ->
//                    run.addPicture(imageInputStream, pictureType, imagePath.fileName.toString(), Units.toEMU(width.toDouble()), Units.toEMU(height.toDouble()))
//                }
//            }


            // 5. Определим путь для сохранения нового документа
            val newDocumentPath = Paths.get("$uploadsDir/${UUID.randomUUID()}.docx")

            // 6. Сохраним новый документ на файловую систему
            Files.newOutputStream(newDocumentPath).use { outputStream ->
                document.write(outputStream)
            }

            return newDocumentPath.fileName.toString()
        }
    }

    // Функция для получения типа изображения по его расширению
    private fun getPictureType(imagePath: Path): Int {
        val fileExtension = imagePath.fileName.toString().substringAfterLast('.').lowercase()

        return when (fileExtension) {
            "png" -> XWPFDocument.PICTURE_TYPE_PNG
            "jpg", "jpeg" -> XWPFDocument.PICTURE_TYPE_JPEG
            "gif" -> XWPFDocument.PICTURE_TYPE_GIF
            "bmp" -> XWPFDocument.PICTURE_TYPE_BMP
            "emf" -> XWPFDocument.PICTURE_TYPE_EMF
            "wmf" -> XWPFDocument.PICTURE_TYPE_WMF
            "pict" -> XWPFDocument.PICTURE_TYPE_PICT
            "tiff" -> XWPFDocument.PICTURE_TYPE_TIFF
            else -> throw IllegalArgumentException("Unsupported image format: $fileExtension")
        }
    }


    fun getAll(applicationStatus: ApplicationStatus = ApplicationStatus.SENT): Iterable<UserApplication> {
        return userApplicationRepository.findAll().filter { it.applicationStatus == applicationStatus }
    }

    fun changeStatus(
        applicationId: Long,
        applicationStatus: ApplicationStatus = ApplicationStatus.SENT
    ): UserApplication {
        val currentApplication = userApplicationRepository.findById(applicationId).getOrNull()
            ?: throw EntityNotFoundException("Entity not found")

        currentApplication.applicationStatus = applicationStatus

        return userApplicationRepository.save(currentApplication)
    }

}
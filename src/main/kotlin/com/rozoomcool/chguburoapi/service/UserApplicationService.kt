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
        val filename = createDocumentWithUserData(reference, currentUser)

        return userApplicationRepository.save(
            UserApplication(
                user = currentUser,
                service = serviceData,
                document = Document(filename = filename),
                applicationStatus = ApplicationStatus.SENT
            )
        )
    }

    // Метод для копирования шаблона и вставки данных пользователя
    private fun createDocumentWithUserData(templatePath: Path, user: User): String {
        val currentDate = LocalDate.now()
        // 1. Открываем шаблон документа
        Files.newInputStream(templatePath).use { inputStream ->
            val document = XWPFDocument(inputStream)

            // 2. Проходим по параграфам и заменяем маркеры данными пользователя
            document.paragraphs.forEach { paragraph ->
                paragraph.runs.forEach { run ->
                    if (run.text().contains("{{fullname}}")) {
                        run.setText(run.text().replace("{{fullname}}", user.profile?.passportData?.fullname ?: ""), 0)
                    }
                    if (run.text().contains("{{userCourse}}")) {
                        run.setText(run.text().replace("{{userCourse}}", "4"), 0)
                    }
                    if (run.text().contains("{{day}}")) {
                        run.setText(run.text().replace("{{day}}", currentDate.dayOfMonth.toString()), 0)
                    }
                    if (run.text().contains("{{month}}")) {
                        run.setText(run.text().replace("{{month}}", currentDate.monthValue.toString()), 0)
                    }
                    if (run.text().contains("{{year}}")) {
                        run.setText(run.text().replace("{{year}}", currentDate.year.toString()), 0)
                    }
                }
            }

            // 3. Определим путь для сохранения нового документа
            val newDocumentPath = Paths.get("$uploadsDir/${UUID.randomUUID()}.docx")

            // 4. Сохраним новый документ на файловую систему
            Files.newOutputStream(newDocumentPath).use { outputStream ->
                document.write(outputStream)
            }

            return newDocumentPath.fileName.toString()
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
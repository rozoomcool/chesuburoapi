package com.rozoomcool.chguburoapi.util.storage

import com.rozoomcool.chguburoapi.exception.StorageException
import com.rozoomcool.chguburoapi.exception.StorageFileNotFoundException
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID
import java.util.stream.Stream

@Service
class FileSystemStorageService(
        private val properties: StorageProperties
) : StorageService {

    private val rootLocation: Path = Paths.get(properties.uploadsLocation.toUri())

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Не удалось инициализировать директорию для хранения файлов.", e)
        }
    }

    override fun store(file: MultipartFile): String {
        try {
            if (file.isEmpty) {
                throw StorageException("Не удалось сохранить пустой файл.")
            }

            val uniqueId = UUID.randomUUID().toString()
            val fileExtension = file.originalFilename?.substringAfterLast('.', "")
            val storedFileName = if (fileExtension.isNullOrEmpty()) uniqueId else "$uniqueId.$fileExtension"

            val destinationFile = rootLocation.resolve(storedFileName).normalize()

            if (!destinationFile.startsWith(rootLocation)) {
                throw StorageException("Попытка сохранить файл за пределы текущей директории.")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }

            return storedFileName
        } catch (e: IOException) {
            throw StorageException("Не удалось сохранить файл.", e)
        }
    }

    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootLocation, 1)
                    .filter { path -> path != rootLocation }
                    .map { rootLocation.relativize(it) }
        } catch (e: IOException) {
            throw StorageException("Не удалось прочитать сохраненные файлы", e)
        }
    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename).normalize()
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            val resource: Resource = UrlResource(file.toUri())
            return if (resource.exists() && resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException("Не удалось прочитать файл: $filename")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Не удалось прочитать файл: $filename", e)
        }
    }

    override fun getResource(filename: String): Resource {
        try {
            // Путь к файлу
            val filePath = rootLocation.resolve(filename).normalize()

            // Проверяем существование и доступность файла
            if (Files.exists(filePath) && Files.isReadable(filePath)) {
                // Возвращаем ресурс
                return UrlResource(filePath.toUri())
            } else {
                throw StorageFileNotFoundException("Файл не найден или недоступен: $filename")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Не удалось прочитать файл: $filename", e)
        }
    }

    override fun deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Не удалось удалить все файлы.", e)
        }
    }

    override fun copyResourceFromTemplates(filename: String, destinationDir: String) {
        try {
            val sourcePath = Paths.get(properties.templatesLocation.toUri()).resolve(filename).normalize()
            val destinationPath = Paths.get(destinationDir).resolve(filename).normalize()

            if (Files.exists(sourcePath) && Files.isReadable(sourcePath)) {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
                println("Файл успешно скопирован в $destinationPath")
            } else {
                throw StorageFileNotFoundException("Шаблон не найден или недоступен: $filename")
            }
        } catch (e: IOException) {
            throw StorageException("Ошибка при копировании файла: $filename", e)
        }
    }
}

package com.rozoomcool.chguburoapi.util.storage

import com.rozoomcool.chguburoapi.exception.StorageException
import com.rozoomcool.chguburoapi.exception.StorageFileNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID
import java.util.stream.Stream
import kotlin.io.path.exists


@Service
class FileSystemStorageService(private val properties: StorageProperties) : StorageService {
    private lateinit var rootLocation: Path

    init {
        if (!properties.location.exists()) {
            Files.createDirectories(properties.location)
            throw StorageException("File upload location can not be Empty.")
        }
    }

    override fun store(file: MultipartFile): String {
        try {
            if (file.isEmpty) {
                throw StorageException("Failed to store empty file.")
            }
            val uniqueId = UUID.randomUUID().toString()
            val fileExtension = file.originalFilename?.substringAfter(".", "")?.let {
                if (it.isNotEmpty()) "$it" else ""
            }
            val storedFileName = "$uniqueId.$fileExtension"

            val destinationFile = rootLocation.resolve(
                Paths.get(file.originalFilename!!)
            ).normalize().toAbsolutePath()

            if (destinationFile.parent != rootLocation.toAbsolutePath()) {
                // This is a security check
                throw StorageException("Cannot store file outside current directory.")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING
                )
            }

            return storedFileName
        } catch (e: IOException) {
            throw StorageException("Failed to store file.", e)
        }
    }

    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootLocation, 1)
                .filter { path: Path -> path != rootLocation }
                .map<Path> { other: Path? ->
                    rootLocation.relativize(
                        other
                    )
                }
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }
    }

    override fun load(filename: String): Path? {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource? {
        return try {
            val file = load(filename)
            val resource: Resource = UrlResource(file!!.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException(
                    "Could not read file: $filename"
                )
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $filename", e)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }
}
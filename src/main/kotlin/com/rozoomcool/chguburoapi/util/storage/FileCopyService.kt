package com.rozoomcool.chguburoapi.util.storage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


@Component
class FileCopyService(private val resourceLoader: ResourceLoader) {
    fun copyResource(resourcePath: String, destinationDir: String) {
        // Загрузка ресурса
        val resource = resourceLoader.getResource(resourcePath)

        if (resource.exists()) {
            val destinationPath = resourceLoader.getResource("classpath:uploads").file.toPath().resolve(resource.filename)

            Files.copy(resource.inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING)
            println("Файл скопирован в $destinationPath")
        } else {
            println("Ресурс не найден: $resourcePath")
        }
    }
}
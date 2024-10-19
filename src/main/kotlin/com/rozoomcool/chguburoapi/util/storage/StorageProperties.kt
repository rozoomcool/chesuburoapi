package com.rozoomcool.chguburoapi.util.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class StorageProperties(
    private val resourseLoader: ResourceLoader,
    @Value("\${dirs.uploads}")
    val uploadsDir: String,
    @Value("\${dirs.templates}")
    val templatesDir: String,
    val uploadsLocation: Path = Path.of(uploadsDir),
    val templatesLocation: Path = Path.of(templatesDir)
)
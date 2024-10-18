package com.rozoomcool.chguburoapi.util.storage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class StorageProperties(
    private val resourseLoader: ResourceLoader,
    val location: Path = resourseLoader.getResource("classpath:uploads").file.toPath()
)
package com.rozoomcool.chguburoapi.util.dataLoder

import com.rozoomcool.chguburoapi.entity.*
import com.rozoomcool.chguburoapi.repository.ServiceDataRepository
import com.rozoomcool.chguburoapi.util.storage.FileSystemStorageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class ServiceDataDataLoader(
        private val resourceLoader: ResourceLoader,
        private val serviceDataRepository: ServiceDataRepository,
        private val fileSystemStorageService: FileSystemStorageService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        addDocuments()
    }

    fun addDocuments() {
        try {
            fileSystemStorageService.copyResourceFromTemplates("mestotrebdoc.docx", "uploads")

            serviceDataRepository.save(
                    ServiceData(
                            forRole = Role.STUDENT,
                            title = "Справка по месту требования",
                            document = Document(filename = "mestotrebdoc.docx")
                    )
            )
        } catch (e: Exception) {
            println(":::::::: Error service data loader ${e.message}")
        }

    }
}
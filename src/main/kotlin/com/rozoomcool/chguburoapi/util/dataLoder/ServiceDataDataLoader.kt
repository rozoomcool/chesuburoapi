package com.rozoomcool.chguburoapi.util.dataLoder

import com.rozoomcool.chguburoapi.entity.*
import com.rozoomcool.chguburoapi.repository.ServiceDataRepository
import com.rozoomcool.chguburoapi.service.ServiceDataService
import com.rozoomcool.chguburoapi.service.UserService
import com.rozoomcool.chguburoapi.util.storage.FileCopyService
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ResourceLoader
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Component
class ServiceDataDataLoader(
        private val resourceLoader: ResourceLoader,
        private val serviceDataRepository: ServiceDataRepository,
        private val fileCopyService: FileCopyService
) : CommandLineRunner {

    val location: Path = resourceLoader.getResource("classpath:templates").file.toPath()
    val uploadDirectory: Path = resourceLoader.getResource("classpath:uploads").file.toPath()

    override fun run(vararg args: String?) {
//        addAdmin()
        addDocuments()
    }

    fun addDocuments() {
        try {
            fileCopyService.copyResource("classpath:templates/mestotrebdoc.docx", "uploads")

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
package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.entity.Document
import com.rozoomcool.chguburoapi.entity.Role
import com.rozoomcool.chguburoapi.entity.ServiceData
import com.rozoomcool.chguburoapi.repository.ServiceDataRepository
import com.rozoomcool.chguburoapi.util.storage.FileSystemStorageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ServiceDataService(
        private val serviceDataRepository: ServiceDataRepository,
        private val fileSystemStorageService: FileSystemStorageService,
        private val resourceLoader: ResourceLoader,
) {
    fun getAll(forRole: Role = Role.STUDENT): Iterable<ServiceData> {
        return serviceDataRepository.findAll().filter {
            it.forRole == forRole
        }
    }

    fun create(serviceData: ServiceData, file: MultipartFile): ServiceData {
        val filename = fileSystemStorageService.store(file)
        serviceData.document = Document(filename = filename)
        return serviceDataRepository.save(serviceData)
    }

    fun loadDocument(filename: String): Resource? {
        return fileSystemStorageService.getResource(filename)
    }
}
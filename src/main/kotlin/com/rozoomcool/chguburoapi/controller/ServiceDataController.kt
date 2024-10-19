package com.rozoomcool.chguburoapi.controller

import com.rozoomcool.chguburoapi.entity.Role
import com.rozoomcool.chguburoapi.entity.ServiceData
import com.rozoomcool.chguburoapi.service.ServiceDataService
import com.rozoomcool.chguburoapi.util.storage.FileSystemStorageService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/service")
class ServiceDataController(
        private val serviceDataService: ServiceDataService,
        private val fileSystemStorageService: FileSystemStorageService
) {
    @GetMapping
    fun getService(@RequestParam("forRole") forRole: Role = Role.STUDENT): ResponseEntity<Iterable<ServiceData>> {
        return ResponseEntity.ok(serviceDataService.getAll(forRole))
    }

    @GetMapping("/document")
    fun getDocument(@RequestParam("filename") filename: String): ResponseEntity<Resource?> {
        val resource = serviceDataService.loadDocument(filename)
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource)
    }
}
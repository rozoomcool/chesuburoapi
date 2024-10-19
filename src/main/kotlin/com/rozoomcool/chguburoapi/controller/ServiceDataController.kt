package com.rozoomcool.chguburoapi.controller

import com.rozoomcool.chguburoapi.entity.Role
import com.rozoomcool.chguburoapi.entity.ServiceData
import com.rozoomcool.chguburoapi.service.ServiceDataService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/document")
class ServiceDataController(
    private val serviceDataService: ServiceDataService
) {
    @GetMapping
    fun getDocument(@RequestParam("forRole") forRole: Role): ResponseEntity<Iterable<ServiceData>> {
        return ResponseEntity.ok(serviceDataService.getAll(forRole))
    }
}
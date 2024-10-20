package com.rozoomcool.chguburoapi.controller

import com.rozoomcool.chguburoapi.entity.ApplicationStatus
import com.rozoomcool.chguburoapi.entity.UserApplication
import com.rozoomcool.chguburoapi.service.UserApplicationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("api/v1/application")
class UserApplicationController(
    private val userApplicationService: UserApplicationService
) {

    @GetMapping
    fun getMy(principal: Principal): Iterable<UserApplication> {
        return userApplicationService.getAll().filter { it.user?.username == principal.name }
    }

    @GetMapping("/a")
    fun getAll(@RequestParam("status") status: ApplicationStatus): ResponseEntity<Iterable<UserApplication>> {
        return ResponseEntity.ok(userApplicationService.getAll(status))
    }

    @PostMapping
    fun create(@RequestParam("serviceId") serviceId: Long, principal: Principal): ResponseEntity<UserApplication> {
        return ResponseEntity.ok(userApplicationService.createApplication(principal.name, serviceId))
    }

    @PutMapping("/a")
    fun changeStatus(
        @RequestParam("appId") appId: Long,
        @RequestParam("status") status: ApplicationStatus
    ): ResponseEntity<UserApplication> {
        return ResponseEntity.ok(userApplicationService.changeStatus(appId, status))
    }
}
package com.rozoomcool.chguburoapi.controller

import com.rozoomcool.chguburoapi.auth.AuthService
import com.rozoomcool.chguburoapi.auth.JwtAuthResponse
import com.rozoomcool.chguburoapi.dto.UserAuthRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/login")
    fun login(@RequestBody userRequest: UserAuthRequest): ResponseEntity<JwtAuthResponse> {
        logger.info("AUTH/LOGIN")
        return ResponseEntity.ok(authService.login(userRequest))
    }

    @PostMapping("/register")
    fun create(@RequestBody userCreateRequest: UserAuthRequest): ResponseEntity<Any> {
        logger.info("AUTH/CREATE")
        return authService.create(userCreateRequest)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestParam refresh: String): ResponseEntity<JwtAuthResponse> {
        logger.info("AUTH/REFRESH")
        return authService.refresh(refresh)
    }
}
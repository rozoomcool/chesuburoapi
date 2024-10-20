package com.rozoomcool.chguburoapi.controller

import com.rozoomcool.chguburoapi.entity.Profile
import com.rozoomcool.chguburoapi.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("api/v1/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping("/all")
    fun getAll(): ResponseEntity<Iterable<Profile>> {
        return ResponseEntity.ok(profileService.getAll())
    }

    @PutMapping
    fun edit(@RequestBody profile: Profile, principal: Principal): ResponseEntity<Profile> {
        return ResponseEntity.ok(profileService.edit(principal.name, profile))
    }

}
package com.rozoomcool.chguburoapi.util.dataLoder

import com.rozoomcool.chguburoapi.entity.Department
import com.rozoomcool.chguburoapi.entity.Profile
import com.rozoomcool.chguburoapi.entity.Role
import com.rozoomcool.chguburoapi.entity.User
import com.rozoomcool.chguburoapi.service.UserService
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder

class ServiceDataDataLoader(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        addAdmin()
    }

    fun addDocuments() {
        
    }
}
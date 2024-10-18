package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.model.CustomPrincipal
import com.rozoomcool.chguburoapi.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username).getOrNull() ?: throw UsernameNotFoundException("User not found")
        return CustomPrincipal(user)
    }
}
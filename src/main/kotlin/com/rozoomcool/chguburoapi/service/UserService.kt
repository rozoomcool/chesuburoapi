package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.repository.UserRepository
import com.rozoomcool.chguburoapi.entity.User
import com.rozoomcool.chguburoapi.exception.EntityAlreadyExistsException
import com.rozoomcool.chguburoapi.exception.EntityBadRequestException
import com.rozoomcool.chguburoapi.exception.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.Throws
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun getPage(): Page<User> = userRepository.findAll(PageRequest.of(0, 10))

    fun getAll(): Iterable<User> = userRepository.findAll()

    fun findByUsername(username: String): User {
        val user = userRepository.findByUsername(username) ?: throw EntityNotFoundException("User not found")
        return user.get()
    }

    fun create(user: User): User {
        if (userRepository.existsByUsername(user.username)) {
            throw EntityAlreadyExistsException("Этот пользователь уже существует")
        }
        try {
            return userRepository.save(user)
        } catch (e: Exception) {
            throw EntityBadRequestException("Неверный зарос")
        }
    }

    fun existsByUsername(username: String): Boolean = userRepository.existsByUsername(username)

}
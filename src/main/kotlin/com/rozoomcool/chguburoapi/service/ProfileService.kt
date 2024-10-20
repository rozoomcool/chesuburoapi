package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.entity.Profile
import com.rozoomcool.chguburoapi.entity.User
import com.rozoomcool.chguburoapi.exception.EntityNotFoundException
import com.rozoomcool.chguburoapi.repository.ProfileRepository
import com.rozoomcool.chguburoapi.repository.UserRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) {

    fun getAll(): Iterable<Profile> {
        return profileRepository.findAll()
    }

    fun edit(username: String, profile: Profile): Profile {
        val currentUser = userRepository.findByUsername(username).getOrNull() ?: throw EntityNotFoundException("Profile not found")
        val profileEntity = currentUser.profile!!

        profileEntity.apply {
            address = profile.address ?: profileEntity.address
            passportData = profile.passportData ?: profileEntity.passportData
            snilsData = profile.snilsData ?: profileEntity.snilsData
            innData = profile.innData ?: profileEntity.innData
        }

        return profileRepository.save(profileEntity)
    }
}
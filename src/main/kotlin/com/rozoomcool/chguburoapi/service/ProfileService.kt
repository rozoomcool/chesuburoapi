package com.rozoomcool.chguburoapi.service

import com.rozoomcool.chguburoapi.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository
) {
}
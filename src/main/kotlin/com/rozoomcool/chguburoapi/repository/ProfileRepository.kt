package com.rozoomcool.chguburoapi.repository

import com.rozoomcool.chguburoapi.entity.Profile
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : CrudRepository<Profile, Long> {
}
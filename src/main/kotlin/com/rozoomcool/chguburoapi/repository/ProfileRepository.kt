package com.rozoomcool.chguburoapi.repository

import com.rozoomcool.chguburoapi.entity.Profile
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : CrudRepository<Profile, Long> {
}
package com.rozoomcool.chguburoapi.repository;

import com.rozoomcool.chguburoapi.entity.UserApplication
import org.springframework.data.repository.CrudRepository

interface UserApplicationRepository : CrudRepository<UserApplication, Long> {
}
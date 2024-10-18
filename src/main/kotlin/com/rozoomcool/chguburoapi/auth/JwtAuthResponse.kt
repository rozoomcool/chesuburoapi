package com.rozoomcool.chguburoapi.auth

import com.rozoomcool.chguburoapi.entity.User

data class JwtAuthResponse(
    val user: User? = null,
    val access: String,
    val refresh: String
)
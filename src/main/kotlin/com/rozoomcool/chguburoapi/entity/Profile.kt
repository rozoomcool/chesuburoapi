package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "profile")
class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var phone: String? = null,
    var dateOfBirth: Date? = null
)
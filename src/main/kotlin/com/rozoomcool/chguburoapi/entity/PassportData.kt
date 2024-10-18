package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "passport_data")
class PassportData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

)
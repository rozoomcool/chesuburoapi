package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "passport_data")
class PassportData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    var fullname: String? = null,
    var gender: Gender? = null,
    var dateOfBirth: Date? = null,
    var placeOfBirth: String? = null,
    var series: String? = null,
    var dateOfIssue: Date? = null,
    var departmentCode: Int? = null,
    var issuedBy: String? = null,

    @OneToOne
    var profile: Profile? = null,
)
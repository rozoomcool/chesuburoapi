package com.rozoomcool.chguburoapi.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.mail.Address
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "profile")
class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var address: String? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JsonManagedReference
    var passportData: PassportData? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JsonManagedReference
    var snilsData: SnilsData? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JsonManagedReference
    var innData: InnData? = null
)
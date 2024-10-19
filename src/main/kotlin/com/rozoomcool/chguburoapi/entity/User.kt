package com.rozoomcool.chguburoapi.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue
    var id: Long? = null,

    @Column(name = "username", unique = true, nullable = false)
    var username: String,

    @OneToOne(cascade = [CascadeType.ALL])
    var profile: Profile? = null,

    @JsonIgnore
    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(value = EnumType.STRING)
    var role: Role,

//    @Enumerated(value = EnumType.STRING)
//    var department: Department
)
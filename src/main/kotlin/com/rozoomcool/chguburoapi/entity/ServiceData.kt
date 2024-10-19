package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*
@Entity
@Table(name = "service")
class ServiceData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var forRole: Role,
    var title: String,
    @OneToOne
    var document: Document? = null
) {
}
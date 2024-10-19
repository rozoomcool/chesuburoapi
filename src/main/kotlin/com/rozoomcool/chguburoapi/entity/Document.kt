package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "document")
class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null
}
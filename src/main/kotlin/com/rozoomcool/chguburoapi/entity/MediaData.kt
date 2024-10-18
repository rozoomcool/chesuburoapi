package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "media_data")
class MediaData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @Column(name = "filename", nullable = false, unique = true)
    var filename: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var owner: User? = null
)
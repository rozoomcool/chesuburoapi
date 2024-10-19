package com.rozoomcool.chguburoapi.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "inn_data")
class InnData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    var series: String? = null,
    @OneToOne
    @JsonBackReference
    var profile: Profile? = null
)
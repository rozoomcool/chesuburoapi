package com.rozoomcool.chguburoapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_application")
class UserApplication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @ManyToOne(cascade = [CascadeType.MERGE])
    var service: ServiceData? = null,

    @Enumerated(value = EnumType.STRING)
    var applicationStatus: ApplicationStatus? = ApplicationStatus.SENT,

    @OneToOne(cascade = [CascadeType.ALL])
    var document: Document? = null,

    @ManyToOne
    var user: User? = null
)
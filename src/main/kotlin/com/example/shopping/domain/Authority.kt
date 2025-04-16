package com.example.shopping.domain

import jakarta.persistence.*

@Entity
@Table(name = "authorities")
class Authority(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    val id: Long = 0,

    val role: Roles? = Roles.USER,

) {

}
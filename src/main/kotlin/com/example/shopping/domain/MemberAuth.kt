package com.example.shopping.domain

import jakarta.persistence.*

@Entity
class MemberAuth(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_auth_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    val authority: Authority
) {


}
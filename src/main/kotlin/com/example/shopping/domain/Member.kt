package com.example.shopping.domain

import jakarta.persistence.*

@Entity
@Table(name = "members")
class Member(

    var loginId: String,

    var loginPw: String?,

    var name: String? = null,

    var mobileNumber: String? = null,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id: Long = 0,

    @OneToMany(mappedBy = "member")
    var roles: MutableList<MemberAuthority>? = null

)
package com.example.shopping.dto

import com.querydsl.core.annotations.QueryProjection

data class MemberDto @QueryProjection constructor (
    val loginId: String,

    var loginPw: String,

    var name: String,

    var mobileNumber: String,

    val id: Long,

//    val roles: MutableList<String>
)


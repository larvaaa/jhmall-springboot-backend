package com.example.shopping.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Member (

    val username: String,

    var phone: String,

    var email: String,

    var address: String,

    var zipcode: String,


) {

    @Id @GeneratedValue
    var id: Long = 0
    


}
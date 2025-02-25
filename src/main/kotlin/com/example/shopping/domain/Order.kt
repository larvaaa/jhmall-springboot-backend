package com.example.shopping.domain

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

class Order (

    var status: OrderStatus = OrderStatus.PAY_COMPLETE

) {

    @Id @GeneratedValue
    @Column(name = "order_id")
    val id: Long = 0


}
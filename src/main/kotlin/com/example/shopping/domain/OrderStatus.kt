package com.example.shopping.domain

enum class OrderStatus (

    val desc: String
) {

    PAY_COMPLETE("결제 완료"),

    PAY_CANCEL("결제 취소"),

    SHIPPING_WAITING("배송 중비 중"),

    SHIPPING_PROGRESS("배송 중"),

    SHIPPING_COMPLETE("배송 완료"),

    BUY_DECISION("구매 결정");
}
package com.example.foodyappkotlin.data.request

import java.io.Serializable

data class MyOrders(
    var ten: String = "",
    var hinhanh: String = "",
    var gia: Long = 0,
    var soluong : Int = 0
) : Serializable

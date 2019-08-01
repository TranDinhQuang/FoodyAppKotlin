package com.example.foodyappkotlin.data.models

import java.io.Serializable


data class BinhLuan(
        var chamdiem: Float = 0F,
        var mauser: String = "",
        var noidung: String = "",
        var tieude: String = "",
        var num_like: Long = 0,
        var num_share: Long = 0,
        var hinhanh: HashMap<String, String> = HashMap()
) : Serializable {
    constructor(id : String, chamdiem: Double, mauser: String, noidung: String, tieude: String, num_like: Long, num_share: Long, hinhanh: Map<String, String>) : this()
}

data class ThaoLuan(
        var mauser: String = "",
        var noidung: String = ""
) : Serializable

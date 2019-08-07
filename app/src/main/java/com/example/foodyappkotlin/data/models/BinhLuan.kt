package com.example.foodyappkotlin.data.models

import java.io.Serializable


data class BinhLuan(
    var id: String = "",
    var chamdiem: Float = 0F,
    var mauser: String = "",
    var noidung: String = "",
    var tieude: String = "",
    var num_like: Long = 0,
    var num_share: Long = 0,
    var user: String = "",
    var num_comment: Long = 0,
    var hinhanh: HashMap<String, String> = HashMap()
) : Serializable


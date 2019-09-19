package com.example.reviewfoodkotlin.data.models

import java.io.Serializable


data class BinhLuan(
    var id: String = "",
    var id_user : String = "",
    var user: String = "",
    var ten_user: String = "",
    var hinhanh_user: String = "",
    var chamdiem: Float = 0F,
    var noidung: String = "",
    var tieude: String = "",
    var hinhanh: HashMap<String, String> = HashMap()
//    var mauser: String = ""
//    var num_like: Long = 0,
//    var num_share: Long = 0,
//    var num_comment: Long = 0,
) : Serializable


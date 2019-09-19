package com.example.reviewfoodkotlin.data.models

import java.io.Serializable

data class ThaoLuan(
    var id_thaoluan: String = "",
    var hinhanh: String = "",
    var noidung: String = "",
    var username: String = "",
    var taikhoan: String = ""
) : Serializable

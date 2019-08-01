package com.example.foodyappkotlin.data.models

import java.io.Serializable


data class BinhLuan(
        var chamdiem: Int = 0,
        var luotthich: Int = 0,
        var mauser: String = "",
        var noidung: String = "",
        var tieude: String = "",
        var num_like: Long = 0,
        var num_share: Long = 0,
        var hinhanh: Map<String, String> = HashMap(),
        var hinhanhbinhluan: ArrayList<String> = ArrayList()
) : Serializable

data class ThaoLuan(
        var mauser: String = "",
        var noidung: String = ""
) : Serializable

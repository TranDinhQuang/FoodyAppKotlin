package com.example.foodyappkotlin.data.models

import java.io.Serializable


data class BinhLuan(
    var id: String = "",
    var chamdiem: Int = 0,
    var luotthich: Int = 0,
    var mauser: String = "",
    var noidung: String = "",
    var tieude: String = ""
) : Serializable

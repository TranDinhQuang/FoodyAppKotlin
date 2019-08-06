package com.example.foodyappkotlin.data.request

import java.io.Serializable

data class QuanAnRequest(
    var idKhuVuc : String = "",
    var idQuanAn : String = "",
    var page : Int = 1,
    var typeCall : Int = 0,
    var valueAt : String = ""
) : Serializable

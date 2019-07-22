package com.example.foodyappkotlin.data.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class QuanAn(
    var id: String = "",
    var tenquanan: String = "",
    var diachi: String = "",
    var giaohang: Boolean = false,
    var giodongcua: String = "",
    var giomocua: String = "",
    var luotthich: Long = 0,
    var num_comments: Long = 0,
    var num_images: Long = 0,
    var danhgia : Double = 0.0,
    var tienich: List<String> = emptyList(),
    var thucdon : String = "",
    var hinhanhquanans: ArrayList<String> = ArrayList(),
    var binhluans: ArrayList<BinhLuan> = ArrayList()
) : Serializable

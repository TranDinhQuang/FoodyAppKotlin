package com.example.foodyappkotlin.data.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class QuanAn(
    var id: String = "",
    var id_khuvuc : Long = 0,
    var tenquanan: String = "",
    var diachi: String = "",
    var giaohang: Boolean = false,
    var giodongcua: Long = 0,
    var giomocua: Long = 0,
    var ngaytao : Long = 0,
    var nguoidang: String = "",
//    var luotthich: Long = 0,
//    var num_comments: Long = 0,
//    var num_images: Long = 0,
    var danhgia: Double = 0.0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var thucdon: String = "",
    var tienich: List<String> = emptyList(),
    var hinhanhs: HashMap<String, String> = HashMap(),
    var binhluans: HashMap<String, BinhLuan> = HashMap(),
    var thucdons : ThucDon = ThucDon(ArrayList(),ArrayList())
) : Serializable

package com.example.foodyappkotlin.data.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ThucDon(
    var monAns: ArrayList<MonAn>,

    val nuocUongs: ArrayList<NuocUong>
) : Serializable

data class MonAn(
    var ten: String = "",
    var hinhanh: String = "",
    var gia: Long = 0
) : Serializable

data class NuocUong(
    var ten: String = "",
    var hinhanh: String = "",
    var gia: Long = 0
) : Serializable

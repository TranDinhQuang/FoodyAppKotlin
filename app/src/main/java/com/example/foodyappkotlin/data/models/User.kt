package com.example.foodyappkotlin.data.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    var taikhoan: String = "",
    var tenhienthi: String = "",
    var hinhanh: String = "",
    var permission: Int = 0,
    var quanan :HashMap<String, String> = HashMap()
) : Serializable
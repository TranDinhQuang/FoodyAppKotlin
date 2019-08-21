package com.example.reviewfoodkotlin.data.response

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ThucDonResponse(
    var id: String = "",
    var ten: String = "",
    var gia: Long = 0,
    var hinhanh: String = "",
    var soluong: Int = 0
) : Serializable
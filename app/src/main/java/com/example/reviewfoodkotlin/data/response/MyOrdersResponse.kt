package com.example.reviewfoodkotlin.data.response

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class MyOrdersResponse(
    var id: String = "",
    var tenquanan: String = "",
    var diadiem: String = "",
    var phiship: Long = 0,
    var thanhtien: Long = 0,
    var monans: Map<String, ThucDonResponse> = HashMap()
) : Serializable

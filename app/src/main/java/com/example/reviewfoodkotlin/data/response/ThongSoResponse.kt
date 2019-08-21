package com.example.reviewfoodkotlin.data.response

import java.io.Serializable

data class ThongSoResponse(
    var num_comment: Long = 0,
    var num_like: Long = 0,
    var num_share: Long = 0
) : Serializable
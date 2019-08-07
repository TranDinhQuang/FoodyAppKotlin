package com.example.foodyappkotlin.data.response

import com.example.foodyappkotlin.data.models.ThaoLuan
import java.io.Serializable

data class ThaoLuanResponse(
    var num_comment: Long = 0,
    var num_like: Long = 0,
    var num_share: Long = 0,
    var thaoluan : HashMap<String,ThaoLuan> = HashMap()
) : Serializable
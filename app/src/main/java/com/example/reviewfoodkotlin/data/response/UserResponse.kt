package com.example.reviewfoodkotlin.data.response
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class UserResponse(
    var userId : String = "",
    var taikhoan: String = "",
    var tenhienthi: String = "",
    var hinhanh: String = "",
    var liked : HashMap<String, String> = HashMap(),
    var quanan : HashMap<String, String> = HashMap(),
    var permission: Int = 0
) : Serializable
package com.example.foodyappkotlin.screen.detail.fragment_post

import com.example.foodyappkotlin.data.models.BinhLuan

interface PostCommentInterface {
    interface View {
        fun postCommentSuccess()
        fun postCommentFailure()
    }

    interface Presenter {
        fun postCommentToServer(view : PostCommentInterface.View,idQuanAn : String,binhLuan: BinhLuan)
    }
}
package com.example.foodyappkotlin.screen.detail.fragment_post

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn

interface PostCommentInterface {
    interface View {
        fun postCommentSuccess()

        fun postCommentFailure()
    }

    interface Presenter {
        fun postCommentToServer(view : PostCommentInterface.View,quanAn : QuanAn,binhLuan: BinhLuan)

        fun editCommentToServer(view : PostCommentInterface.View,quanAn : QuanAn,binhLuan: BinhLuan)
    }
}
package com.example.reviewfoodkotlin.screen.detail.fragment_post

import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.models.QuanAn

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
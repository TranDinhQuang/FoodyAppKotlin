package com.example.reviewfoodkotlin.screen.detail.fragment_detail_comment

import com.example.reviewfoodkotlin.data.models.ThaoLuan

interface DetailCommentInterface {
    interface View {
        fun getThaoLuanSuccess(data: ThaoLuan)

        fun getThaoLuanFailure(message: String)
    }

    interface Presenter {
        fun getThaoLuan(idQuanAn: String, idBinhLuan: String, view: DetailCommentInterface.View)
    }
}
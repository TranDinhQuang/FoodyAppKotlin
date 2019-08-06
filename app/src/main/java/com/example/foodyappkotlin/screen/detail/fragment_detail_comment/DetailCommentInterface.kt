package com.example.foodyappkotlin.screen.detail.fragment_detail_comment

import com.example.foodyappkotlin.data.models.ThaoLuan

interface DetailCommentInterface {
    interface View {
        fun getThaoLuanSuccess(data: ThaoLuan)

        fun getThaoLuanFailure(message: String)
    }

    interface Presenter {
        fun getThaoLuan(idQuanAn: String, idBinhLuan: String, view: DetailCommentInterface.View)
    }
}
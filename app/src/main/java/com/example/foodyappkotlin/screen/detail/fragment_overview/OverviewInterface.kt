package com.example.foodyappkotlin.screen.detail.fragment_overview

import com.example.foodyappkotlin.data.models.BinhLuan

interface OverviewInterface {
    interface View {
        fun getAllCommentSuccess(comment : BinhLuan)

        fun getAllCommentFailure(message : String)
    }

    interface Presenter {
        fun getAllCommentFollowQuanAn(idQuanAn: String, view: OverviewInterface.View)
    }
}
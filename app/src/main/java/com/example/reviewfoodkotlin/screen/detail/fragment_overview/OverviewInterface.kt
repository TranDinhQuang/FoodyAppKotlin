package com.example.reviewfoodkotlin.screen.detail.fragment_overview

import com.example.reviewfoodkotlin.data.models.BinhLuan

interface OverviewInterface {
    interface View {
        fun getAllCommentSuccess(comment : BinhLuan)

        fun getAllCommentFailure(message : String)
    }

    interface Presenter {
        fun getAllCommentFollowQuanAn(idQuanAn: String, view: OverviewInterface.View)
    }
}
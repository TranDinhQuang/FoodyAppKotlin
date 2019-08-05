package com.example.foodyappkotlin.screen.detail.fragment_comments

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource

class CommentsPresenter(val repository: FoodyRepository, val view: FragmentComments) {
    fun getAllComment(maQuanAn: String) {
        repository.getAllCommentFollowQuanAn(maQuanAn,
            object : FoodyDataSource.DataCallBack<BinhLuan> {
                override fun onSuccess(data: BinhLuan) {
                    view.getAllCommentSuccess(data)
                }

                override fun onFailure(message: String) {
                    view.getAllCommentFailure(message)
                }
            })
    }
}
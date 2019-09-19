package com.example.reviewfoodkotlin.screen.detail.fragment_comments

import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.source.FoodyDataSource

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
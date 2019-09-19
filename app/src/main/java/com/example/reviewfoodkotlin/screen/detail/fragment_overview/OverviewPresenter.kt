package com.example.reviewfoodkotlin.screen.detail.fragment_overview

import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.source.FoodyDataSource
import com.example.reviewfoodkotlin.di.scope.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class OverviewPresenter @Inject constructor(val repository: FoodyRepository) : OverviewInterface.Presenter{
    override fun getAllCommentFollowQuanAn(idQuanAn: String, view: OverviewInterface.View) {
        repository.getAllCommentFollowQuanAn(idQuanAn,object : FoodyDataSource.DataCallBack<BinhLuan>{
            override fun onSuccess(data: BinhLuan) {
                view.getAllCommentSuccess(data)
            }

            override fun onFailure(message: String) {
                view.getAllCommentFailure(message)
            }

        })
    }

}
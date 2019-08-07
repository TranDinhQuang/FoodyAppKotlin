package com.example.foodyappkotlin.screen.detail.fragment_overview

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.di.scope.ActivityScoped
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
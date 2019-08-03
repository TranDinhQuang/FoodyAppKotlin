package com.example.foodyappkotlin.screen.detail.fragment_post

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.di.scope.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PostCommentPresenter @Inject constructor(val repository: FoodyRepository) : PostCommentInterface.Presenter {
    override fun postCommentToServer(view : PostCommentInterface.View,quanAn: QuanAn,binhLuan: BinhLuan) {
        repository.writeCommentToDataBase(quanAn, binhLuan, object : FoodyDataSource.DataCallBack<String> {
            override fun onSuccess(data: String) {
                view.postCommentSuccess()
            }

            override fun onFailure(message: String) {
                view.postCommentFailure()
            }
        })
    }

}
package com.example.reviewfoodkotlin.screen.detail.fragment_post

import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.source.FoodyDataSource
import com.example.reviewfoodkotlin.di.scope.ActivityScoped
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

    override fun editCommentToServer(view : PostCommentInterface.View,quanAn: QuanAn,binhLuan: BinhLuan) {
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
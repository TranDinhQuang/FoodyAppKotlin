package com.example.foodyappkotlin.screen.detail.fragment_detail_comment

import com.example.foodyappkotlin.data.models.ThaoLuan
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource

class DetailCommentPresenter(val repository: FoodyRepository) : DetailCommentInterface.Presenter {

    override fun getThaoLuan(
        idQuanAn: String,
        idBinhLuan: String,
        view: DetailCommentInterface.View
    ) {
        repository.getThaoLuanIntoComment(
            idQuanAn,
            idBinhLuan,
            object : FoodyDataSource.DataCallBack<ThaoLuan> {
                override fun onSuccess(data: ThaoLuan) {
                    view.getThaoLuanSuccess(data)
                }

                override fun onFailure(message: String) {
                    view.getThaoLuanFailure(message)
                }

            })
    }

}
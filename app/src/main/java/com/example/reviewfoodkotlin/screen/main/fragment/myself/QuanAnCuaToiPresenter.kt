package com.example.reviewfoodkotlin.screen.main.fragment.myself

import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.request.QuanAnRequest
import com.example.reviewfoodkotlin.data.source.FoodyDataSource

class QuanAnCuaToiPresenter(val repository: FoodyRepository) {
    fun getQuanAnFollowId(quanAnRequest: QuanAnRequest, view: QuanAnCuaToiFragment) {
        repository.getQuanAnsFollowId(quanAnRequest, object : FoodyDataSource.DataCallBack<QuanAn> {
            override fun onSuccess(data: QuanAn) {
                view.getQuanAnSuccess(data)
            }

            override fun onFailure(message: String) {
                view.getQuanAnFailure(message)
            }
        })
    }
}
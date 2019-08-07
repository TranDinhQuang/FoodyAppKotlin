package com.example.foodyappkotlin.screen.main.fragment.myself

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.source.FoodyDataSource

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
package com.example.foodyappkotlin.screen.main.fragment

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.source.FoodyDataSource

class ODauPresenter(val repository: FoodyRepository, val view: ODauInterface.View) :
    ODauInterface.Presenter {
    override fun getQuanAns(quanAnRequest: QuanAnRequest) {
        repository.getQuanAns(quanAnRequest, object : FoodyDataSource.DataCallBack<MutableList<QuanAn>> {
            override fun onSuccess(data: MutableList<QuanAn>) {
                view.QuanAnsSuccess(data)
            }

            override fun onFailure(message: String) {
                view.QuanAnsFailure(message)
            }

        })
    }
}

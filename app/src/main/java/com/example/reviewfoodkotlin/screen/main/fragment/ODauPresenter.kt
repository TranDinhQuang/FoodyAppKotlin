package com.example.reviewfoodkotlin.screen.main.fragment

import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.request.QuanAnRequest
import com.example.reviewfoodkotlin.data.source.FoodyDataSource
import com.example.reviewfoodkotlin.di.scope.FragmentScoped

@FragmentScoped
class ODauPresenter(val repository: FoodyRepository, val view: ODauInterface.View) :
    ODauInterface.Presenter {
    var isRemovedListener = false
    override fun removeListernerQuanAn() {
        isRemovedListener = true
    }

    override fun getQuanAns(quanAnRequest: QuanAnRequest) {
        isRemovedListener = false
        repository.getQuanAns(
            quanAnRequest,
            object : FoodyDataSource.DataCallBack<MutableList<QuanAn>> {
                override fun onSuccess(data: MutableList<QuanAn>) {
                    if (isRemovedListener) {
                        return
                    }
                    view.QuanAnsSuccess(data)
                }

                override fun onFailure(message: String) {
                    if (isRemovedListener) {
                        return
                    }
                    view.QuanAnsFailure(message)
                }

            })
    }


}

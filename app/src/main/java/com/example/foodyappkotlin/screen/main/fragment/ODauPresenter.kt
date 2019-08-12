package com.example.foodyappkotlin.screen.main.fragment

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.di.scope.FragmentScoped

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

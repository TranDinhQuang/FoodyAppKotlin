package com.example.foodyappkotlin.screen.detail

import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource

class DetailEatingPresenter(val maThucDon : String,val repository: FoodyRepository, val view: DetailEatingInterface.View) :
    DetailEatingInterface.Presenter {
    override fun getThucDons() {
        repository.getThucDons(maThucDon,object : FoodyDataSource.DataCallBack<ThucDon> {
            override fun onSuccess(data: ThucDon) {
                view.thucDonsSuccess(data)
            }

            override fun onFailure(message: String) {
            view.thucDonsFailure(message)
            }
        })
    }
}

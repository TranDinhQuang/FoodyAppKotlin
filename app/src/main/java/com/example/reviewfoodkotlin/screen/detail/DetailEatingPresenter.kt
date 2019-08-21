package com.example.reviewfoodkotlin.screen.detail

import com.example.reviewfoodkotlin.data.repository.FoodyRepository

class DetailEatingPresenter(
    val maThucDon: String,
    val repository: FoodyRepository,
    val view: DetailEatingInterface.View
) :
    DetailEatingInterface.Presenter {
    override fun getThucDons() {

    }
    /* override fun getThucDons() {
         repository.getThucDons(maThucDon,
             object : FoodyDataSource.DataCallBack<MutableList<ThucDonResponse>> {
                 override fun onSuccess(data: MutableList<ThucDonResponse>) {
                     view.thucDonsSuccess(data)
                 }

                 override fun onFailure(message: String) {
                     view.thucDonsFailure(message)
                 }
             })
     }*/
}

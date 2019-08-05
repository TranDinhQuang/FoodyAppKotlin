package com.example.foodyappkotlin.screen.main.fragment.search

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource

class SearchPresenter(val repository: FoodyRepository) : SearchInterface.Presenter {
    override fun searchQuanAn(
        view: SearchInterface.View,
        idKhuVuc: String,
        textSearch: String,
        type: Int
    ) {
        repository.searchQuanAn(idKhuVuc,textSearch,type,object : FoodyDataSource.DataCallBack<MutableList<QuanAn>>{
            override fun onSuccess(data: MutableList<QuanAn>) {
                view.searchSuccess(data)
            }

            override fun onFailure(message: String) {
            view.searchFailure(message)
            }

        })
    }

}
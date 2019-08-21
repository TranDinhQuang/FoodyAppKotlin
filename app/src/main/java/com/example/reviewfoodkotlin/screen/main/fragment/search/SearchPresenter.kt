package com.example.reviewfoodkotlin.screen.main.fragment.search

import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.source.FoodyDataSource

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
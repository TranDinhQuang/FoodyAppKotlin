package com.example.foodyappkotlin.screen.main.fragment.search

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.source.FoodyDataSource

interface SearchInterface {
    interface View {
        fun searchSuccess(quanAn: MutableList<QuanAn>)
        fun searchFailure(message: String)
    }

    interface Presenter {
        fun searchQuanAn(
            view: SearchInterface.View,
            idKhuVuc: String,
            textSearch: String,
            type: Int
        )
    }
}
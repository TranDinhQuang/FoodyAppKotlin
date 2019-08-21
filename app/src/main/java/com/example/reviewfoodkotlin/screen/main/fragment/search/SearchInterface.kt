package com.example.reviewfoodkotlin.screen.main.fragment.search

import com.example.reviewfoodkotlin.data.models.QuanAn

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
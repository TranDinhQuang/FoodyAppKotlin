package com.example.foodyappkotlin.data.source

import com.example.foodyappkotlin.data.models.QuanAn

interface FoodyDataSource {
    interface DataCallBack<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String)
    }

    interface Remote {
        fun getQuanAns(callback: DataCallBack<List<QuanAn>>)
    }
}

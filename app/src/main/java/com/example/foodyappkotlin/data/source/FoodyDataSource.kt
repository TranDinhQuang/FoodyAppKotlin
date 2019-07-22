package com.example.foodyappkotlin.data.source

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon

interface FoodyDataSource {
    interface DataCallBack<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String)
    }

    interface Remote {
        fun getQuanAns(province : Int,page : Int,callback: DataCallBack<List<QuanAn>>)
        fun getThucDons(maQuanAn: String, callback: DataCallBack<ThucDon>)
        fun getHinhAnhBinhLuan(callBack: DataCallBack<List<String>>)
    }
}

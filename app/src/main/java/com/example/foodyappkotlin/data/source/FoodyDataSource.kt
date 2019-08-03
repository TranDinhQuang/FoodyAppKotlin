package com.example.foodyappkotlin.data.source

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.models.User

interface FoodyDataSource {
    interface DataCallBack<T> {
        fun onSuccess(data: T)

        fun onFailure(message: String)
    }

    interface Remote {
        fun getQuanAns(province: Int, page: Int, callback: DataCallBack<List<QuanAn>>)

        fun getAllCommentFollowQuanAn(idQuanan: String, callback: DataCallBack<BinhLuan>)

        fun getThucDons(maQuanAn: String, callback: DataCallBack<ThucDon>)

        fun getHinhAnhBinhLuan(callBack: DataCallBack<List<String>>)

        fun writeCommentToDataBase(
            idQuanAn: QuanAn, binhluan: BinhLuan, callBack: DataCallBack<String>
        )

        fun saveUserLoginData(user : User,callBack: DataCallBack<User>)

    }
}

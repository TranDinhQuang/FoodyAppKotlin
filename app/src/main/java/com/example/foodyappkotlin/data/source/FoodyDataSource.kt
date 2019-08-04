package com.example.foodyappkotlin.data.source

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.models.User
import com.example.foodyappkotlin.data.response.UserResponse

interface FoodyDataSource {
    interface DataCallBack<T> {
        fun onSuccess(data: T)

        fun onFailure(message: String)
    }

    interface Remote {
        fun addQuanAnMyself(idKhuVuc : String,quanAn: QuanAn,callback: DataCallBack<MutableList<String>>)

        fun getQuanAns(province: Int, page: Int,valueAt : String, callback: DataCallBack<MutableList<QuanAn>>)

        fun getAllCommentFollowQuanAn(idQuanan: String, callback: DataCallBack<BinhLuan>)

        fun getThucDons(maQuanAn: String, callback: DataCallBack<ThucDon>)

        fun getHinhAnhBinhLuan(callBack: DataCallBack<List<String>>)

        fun writeCommentToDataBase(
            idQuanAn: QuanAn, binhluan: BinhLuan, callBack: DataCallBack<String>
        )

        fun saveUserLoginData(user : User,callBack: DataCallBack<UserResponse>)

        fun getListLikedOfUser(userId : String,callBack: DataCallBack<MutableList<String>>)

    }
}

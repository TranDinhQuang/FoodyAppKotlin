package com.example.foodyappkotlin.data.source

import com.example.foodyappkotlin.data.models.*
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.data.response.UserResponse

interface FoodyDataSource {
    interface DataCallBack<T> {
        fun onSuccess(data: T)

        fun onFailure(message: String)
    }

    interface Remote {
        fun getThaoLuanIntoComment(idQuanan: String,idBinhLuan: String,callback: DataCallBack<ThaoLuan>)

        fun searchQuanAn(idKhuVuc : String,textSearch : String,type : Int,callback: DataCallBack<MutableList<QuanAn>>)

        fun addQuanAnMyself(idKhuVuc : String,quanAn: QuanAn,callback: DataCallBack<MutableList<String>>)

        fun getQuanAns(quanAnRequest: QuanAnRequest, callback: DataCallBack<MutableList<QuanAn>>)

        fun getQuanAnsFollowId(quanAnRequest: QuanAnRequest, callback: DataCallBack<QuanAn>)

        fun getAllCommentFollowQuanAn(idQuanan: String, callback: DataCallBack<BinhLuan>)

        fun getThucDons(maQuanAn: String, callback: DataCallBack<MutableList<ThucDonResponse>>)

        fun getHinhAnhBinhLuan(callBack: DataCallBack<List<String>>)

        fun writeCommentToDataBase(
            idQuanAn: QuanAn, binhluan: BinhLuan, callBack: DataCallBack<String>
        )

        fun saveUserLoginData(user : User,callBack: DataCallBack<UserResponse>)

        fun getUser(userId : String,callBack: DataCallBack<UserResponse>)

    }
}

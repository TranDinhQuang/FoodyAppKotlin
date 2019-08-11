package com.example.foodyappkotlin.data.repository

import com.example.foodyappkotlin.data.models.*
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.data.response.UserResponse
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.haipq.miniweather.data.source.Remote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodyRepository @Inject constructor(
    @Remote private val remoteRepo: FoodyDataSource.Remote
) : FoodyDataSource.Remote {

    override fun getQuanAnsFollowNguoiDang(
        idKhuVuc: String,
        callback: FoodyDataSource.DataCallBack<QuanAn>
    ) {

    }

    override fun getQuanAnsFollowId(
        quanAnRequest: QuanAnRequest,
        callback: FoodyDataSource.DataCallBack<QuanAn>
    ) {
        remoteRepo.getQuanAnsFollowId(quanAnRequest,callback)
    }

    override fun getThaoLuanIntoComment(
        idQuanan: String,
        idBinhLuan: String,
        callback: FoodyDataSource.DataCallBack<ThaoLuan>
    ) {
        remoteRepo.getThaoLuanIntoComment(idQuanan, idBinhLuan, callback)
    }

    override fun searchQuanAn(
        idKhuVuc: String,
        textSearch: String,
        type: Int,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        remoteRepo.searchQuanAn(idKhuVuc, textSearch, type, callback)
    }

    override fun getUser(
        userId: String,
        callBack: FoodyDataSource.DataCallBack<UserResponse>
    ) {
        remoteRepo.getUser(userId, callBack)
    }

    override fun saveUserLoginData(
        user: User,
        callBack: FoodyDataSource.DataCallBack<UserResponse>
    ) {
        remoteRepo.saveUserLoginData(user, callBack)
    }

    override fun getAllCommentFollowQuanAn(
        idQuanan: String,
        callback: FoodyDataSource.DataCallBack<BinhLuan>
    ) {
        remoteRepo.getAllCommentFollowQuanAn(idQuanan, callback)
    }

    override fun addQuanAnMyself(
        idKhuVuc: String,
        quanAn: QuanAn,
        callback: FoodyDataSource.DataCallBack<MutableList<String>>
    ) {
        remoteRepo.addQuanAnMyself(idKhuVuc, quanAn, callback)
    }

    override fun writeCommentToDataBase(
        idQuanAn: QuanAn,
        binhluan: BinhLuan,
        callBack: FoodyDataSource.DataCallBack<String>
    ) {
        remoteRepo.writeCommentToDataBase(
            idQuanAn, binhluan, callBack
        )
    }

    override fun getHinhAnhBinhLuan(callBack: FoodyDataSource.DataCallBack<List<String>>) {
    }

    override fun getThucDons(idThucDon: String, callback: FoodyDataSource.DataCallBack<MutableList<ThucDonResponse>>) {
        remoteRepo.getThucDons(idThucDon, callback)
    }

    override fun getQuanAns(
        quanAnRequest: QuanAnRequest,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        remoteRepo.getQuanAns(quanAnRequest, callback)
    }
}

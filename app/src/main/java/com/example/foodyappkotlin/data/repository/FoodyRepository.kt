package com.example.foodyappkotlin.data.repository

import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.models.User
import com.example.foodyappkotlin.data.response.UserResponse
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.haipq.miniweather.data.source.Remote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodyRepository @Inject constructor(
    @Remote private val remoteRepo: FoodyDataSource.Remote
) : FoodyDataSource.Remote {
    override fun searchQuanAn(
        idKhuVuc: String,
        textSearch: String,
        type: Int,
        callback: FoodyDataSource.DataCallBack<QuanAn>
    ) {
        remoteRepo.searchQuanAn(idKhuVuc,textSearch,type,callback)
    }

    override fun getListLikedOfUser(
        userId: String,
        callBack: FoodyDataSource.DataCallBack<MutableList<String>>
    ) {
        remoteRepo.getListLikedOfUser(userId, callBack)
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

    override fun addQuanAnMyself(idKhuVuc : String,
        quanAn: QuanAn,
        callback: FoodyDataSource.DataCallBack<MutableList<String>>
    ) {
        remoteRepo.addQuanAnMyself(idKhuVuc,quanAn, callback)
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

    override fun getThucDons(maQuanAn: String, callback: FoodyDataSource.DataCallBack<ThucDon>) {
        remoteRepo.getThucDons(maQuanAn, callback)
    }

    override fun getQuanAns(
        province: Int,
        page: Int,valueAt : String,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        remoteRepo.getQuanAns(province, page,valueAt, callback)
    }
}

package com.example.foodyappkotlin.data.repository

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.haipq.miniweather.data.source.Remote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodyRepository @Inject constructor(
    @Remote private val remoteRepo: FoodyDataSource.Remote
) : FoodyDataSource.Remote {
    override fun getHinhAnhBinhLuan(callBack: FoodyDataSource.DataCallBack<List<String>>) {
    }

    override fun getThucDons(maQuanAn : String,callback: FoodyDataSource.DataCallBack<ThucDon>) {
        remoteRepo.getThucDons(maQuanAn,callback)
    }

    override fun getQuanAns(province : Int,page : Int,callback: FoodyDataSource.DataCallBack<List<QuanAn>>) {
        remoteRepo.getQuanAns(province,page,callback)
    }
}

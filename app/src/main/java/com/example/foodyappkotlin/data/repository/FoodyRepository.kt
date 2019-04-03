package com.example.foodyappkotlin.data.repository

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.haipq.miniweather.data.source.Remote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodyRepository @Inject constructor(
    @Remote private val remoteRepo: FoodyDataSource.Remote
) : FoodyDataSource.Remote {
    override fun getQuanAns(): List<QuanAn> {
        return remoteRepo.getQuanAns()
    }

}
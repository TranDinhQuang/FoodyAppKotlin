package com.example.reviewfoodkotlin.screen.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.models.QuanAn

class DetailViewModel : ViewModel() {
    val quanan = MutableLiveData<QuanAn>()

    val binhLuan = MutableLiveData<BinhLuan>()

    fun setQuanan(item: QuanAn) {
        quanan.value = item
    }

    fun setBinhluan(item: BinhLuan) {
        binhLuan.value = item
    }
}

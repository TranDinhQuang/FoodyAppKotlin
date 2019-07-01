package com.example.foodyappkotlin.screen.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon

class DetailViewModel : ViewModel() {
    val quanan = MutableLiveData<QuanAn>()
    val thucdon = MutableLiveData<ThucDon>()

    fun setQuanan(item: QuanAn) {
        quanan.value = item
    }

    fun setThucDon(item: ThucDon){
        thucdon.value = item
    }
}

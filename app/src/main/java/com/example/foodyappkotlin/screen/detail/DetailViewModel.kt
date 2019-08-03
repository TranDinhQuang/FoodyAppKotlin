package com.example.foodyappkotlin.screen.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon

class DetailViewModel : ViewModel() {
    val quanan = MutableLiveData<QuanAn>()

    fun setQuanan(item: QuanAn) {
        quanan.value = item
    }
}

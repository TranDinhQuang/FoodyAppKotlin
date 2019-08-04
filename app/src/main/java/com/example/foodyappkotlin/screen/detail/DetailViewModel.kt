package com.example.foodyappkotlin.screen.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.response.UserResponse

class DetailViewModel : ViewModel() {
    val quanan = MutableLiveData<QuanAn>()
    val user = MutableLiveData<UserResponse>()

    fun setQuanan(item: QuanAn) {
        quanan.value = item
    }

    fun setUser(item: UserResponse) {
        user.value = item
    }
}

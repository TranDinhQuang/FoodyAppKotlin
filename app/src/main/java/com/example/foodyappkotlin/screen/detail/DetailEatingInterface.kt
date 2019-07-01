package com.example.foodyappkotlin.screen.detail

import com.example.foodyappkotlin.data.models.ThucDon

interface DetailEatingInterface {
    interface View {
        fun thucDonsSuccess(thucdons : ThucDon)
        fun thucDonsFailure(message : String)
    }

    interface Presenter {
        fun getThucDons()
    }
}

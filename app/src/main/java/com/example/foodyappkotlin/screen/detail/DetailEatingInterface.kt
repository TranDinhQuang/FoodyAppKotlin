package com.example.foodyappkotlin.screen.detail

import com.example.foodyappkotlin.data.response.ThucDonResponse

interface DetailEatingInterface {
    interface View {
        fun thucDonsSuccess(thucdons: MutableList<ThucDonResponse>)

        fun thucDonsFailure(message: String)
    }

    interface Presenter {
        fun getThucDons()
    }
}

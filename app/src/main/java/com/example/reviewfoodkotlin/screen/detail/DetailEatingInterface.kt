package com.example.reviewfoodkotlin.screen.detail

import com.example.reviewfoodkotlin.data.response.ThucDonResponse

interface DetailEatingInterface {
    interface View {
        fun thucDonsSuccess(thucdons: MutableList<ThucDonResponse>)

        fun thucDonsFailure(message: String)
    }

    interface Presenter {
        fun getThucDons()
    }
}

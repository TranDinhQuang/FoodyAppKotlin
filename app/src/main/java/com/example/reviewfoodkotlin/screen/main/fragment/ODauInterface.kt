package com.example.reviewfoodkotlin.screen.main.fragment

import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.request.QuanAnRequest

interface ODauInterface {
    interface View{
        fun QuanAnsSuccess(quanans : MutableList<QuanAn>)

        fun QuanAnsFailure(msg : String)
    }

    interface Presenter{
        fun getQuanAns(quanAnRequest: QuanAnRequest)

        fun removeListernerQuanAn()
    }
}

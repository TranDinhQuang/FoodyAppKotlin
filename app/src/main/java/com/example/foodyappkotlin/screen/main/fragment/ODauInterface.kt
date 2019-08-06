package com.example.foodyappkotlin.screen.main.fragment

import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.request.QuanAnRequest

interface ODauInterface {
    interface View{
        fun QuanAnsSuccess(quanans : MutableList<QuanAn>)

        fun QuanAnsFailure(msg : String)
    }

    interface Presenter{
        fun getQuanAns(quanAnRequest: QuanAnRequest)
    }
}

package com.example.foodyappkotlin.screen.main.fragment

import com.example.foodyappkotlin.data.models.QuanAn

interface ODauInterface {
    interface View{
        fun QuanAnsSuccess(quanans : List<QuanAn>)
        fun QuanAnsFailure()
    }

    interface Presenter{
        fun getQuanAns()
    }
}
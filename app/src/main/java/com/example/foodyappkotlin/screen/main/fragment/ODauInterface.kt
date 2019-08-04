package com.example.foodyappkotlin.screen.main.fragment

import com.example.foodyappkotlin.data.models.QuanAn

interface ODauInterface {
    interface View{
        fun QuanAnsSuccess(quanans : MutableList<QuanAn>)
        fun QuanAnsFailure(msg : String)
    }

    interface Presenter{
        fun getQuanAns(khuvuc : Int,page : Int,valueAt : String)
    }
}

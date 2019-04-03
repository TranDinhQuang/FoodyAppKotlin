package com.example.foodyappkotlin.data.source

import com.example.foodyappkotlin.data.models.QuanAn

interface FoodyDataSource {
    interface Remote {
        fun getQuanAns(): List<QuanAn>
    }
}
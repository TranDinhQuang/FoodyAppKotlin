package com.example.foodyappkotlin.screen.login

import com.example.foodyappkotlin.data.models.User

interface LoginInterface {
    interface View {
        fun userInfoSuccess(user: User)

        fun serverFailure(msg: String)
    }

    interface Presenter {

        fun saveDataLogin(user: User)
    }
}
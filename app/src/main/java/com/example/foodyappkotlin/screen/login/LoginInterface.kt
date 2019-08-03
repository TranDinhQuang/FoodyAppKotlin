package com.example.foodyappkotlin.screen.login

import com.example.foodyappkotlin.data.models.User
import com.example.foodyappkotlin.data.response.UserResponse

interface LoginInterface {
    interface View {
        fun userInfoSuccess(user: UserResponse)

        fun serverFailure(msg: String)
    }

    interface Presenter {

        fun saveDataLogin(user: User)
    }
}
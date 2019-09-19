package com.example.reviewfoodkotlin.screen.login

import com.example.reviewfoodkotlin.data.models.User
import com.example.reviewfoodkotlin.data.response.UserResponse

interface LoginInterface {
    interface View {
        fun userInfoSuccess(user: UserResponse)

        fun serverFailure(msg: String)
    }

    interface Presenter {

        fun saveDataLogin(user: User)
    }
}
package com.example.reviewfoodkotlin.screen.login

import com.example.reviewfoodkotlin.data.models.User
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.response.UserResponse
import com.example.reviewfoodkotlin.data.source.FoodyDataSource

class LoginPresenter(val repository: FoodyRepository, val view: LoginInterface.View) :
    LoginInterface.Presenter {
    override fun saveDataLogin(user: User) {
        repository.saveUserLoginData(user, object : FoodyDataSource.DataCallBack<UserResponse> {
            override fun onSuccess(user: UserResponse) {
                view.userInfoSuccess(user)
            }

            override fun onFailure(message: String) {
                view.serverFailure(message)
            }

        })
    }
}
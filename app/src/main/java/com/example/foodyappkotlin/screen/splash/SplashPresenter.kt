package com.example.foodyappkotlin.screen.splash

import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.UserResponse
import com.example.foodyappkotlin.data.source.FoodyDataSource

class SplashPresenter(val repository: FoodyRepository,val view : SplashActivity) {
    fun getUserLogin(userId : String){
        repository.getUser(userId,object : FoodyDataSource.DataCallBack<UserResponse>{
            override fun onSuccess(data: UserResponse) {
                view.getUserSuccess(data)
            }

            override fun onFailure(message: String) {
                view.getUserFailure(message)
            }
        })
    }
}
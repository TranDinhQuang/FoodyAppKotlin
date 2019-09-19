package com.example.reviewfoodkotlin.screen.splash

import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.response.UserResponse
import com.example.reviewfoodkotlin.data.source.FoodyDataSource

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
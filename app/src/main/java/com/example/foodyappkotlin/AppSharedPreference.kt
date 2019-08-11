package com.example.foodyappkotlin

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import com.example.foodyappkotlin.data.models.User
import com.example.foodyappkotlin.data.response.UserResponse
import javax.inject.Inject

class AppSharedPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val TAG = AppSharedPreference::class.java.name
        private const val KeyPhoneNumber = "phoneNumber"
        private const val KeyToken = "token"
        private const val KeyUserName = "userName"
    }

    private var tokenAuth: String? = null

    private var phoneNumber: String? = null

    private var userName: String? = null

    private lateinit var location: Location

    private lateinit var user: UserResponse

    fun getUserName(): String? {
        if (userName == null) {
            userName = sharedPreferences.getString(KeyUserName, "")
        }
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
        sharedPreferences.edit().putString(KeyUserName, userName).apply()
    }

    fun getPhoneNumber(): String? {
        if (phoneNumber == null) {
            phoneNumber = sharedPreferences.getString(KeyPhoneNumber, "")
        }
        return phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String?) {
        this.phoneNumber = phoneNumber
        sharedPreferences.edit().putString(KeyPhoneNumber, phoneNumber).apply()
    }

    fun getToken(): String? {
        Log.i(TAG, "getToken token=$tokenAuth")
        if (tokenAuth == null) {
            tokenAuth = sharedPreferences.getString(KeyToken, "")
        }
        return tokenAuth
    }

    fun setToken(token: String?) {
        Log.i(TAG, "setToken token=$token")
        tokenAuth = token
        sharedPreferences.edit().putString(KeyToken, token).apply()
    }

    fun setLocation(location: Location) {
        val defaultLocation = Location("")
        defaultLocation.latitude = 21.026047
        defaultLocation.longitude =105.81267
        if(location != null){
            this.location = location
        }else{
            this.location  = defaultLocation
        }
    }

    fun getLocation() : Location{
        return location
    }

    fun setUser(user: UserResponse) {
        this.user = user
    }

    fun getUser() : UserResponse{
        return user
    }
}

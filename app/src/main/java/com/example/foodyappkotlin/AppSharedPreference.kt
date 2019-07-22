package com.example.foodyappkotlin

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import javax.inject.Inject

class AppSharedPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val TAG = AppSharedPreference::class.java.name
        private const val KeyPhoneNumber = "phoneNumber"
        private const val KeyToken = "token"
        private const val KeyFcm = "fcmToken"
        private const val KeyUserName = "userName"
    }

    private var tokenAuth: String? = null

    private var phoneNumber: String? = null

    private var fcmToken: String? = null

    private var userName: String? = null

    private lateinit var location: Location
    fun getUserName(): String? {
        Log.d("kiemtra", "vao day")
        /*if (userName == null) {
            userName = sharedPreferences.getString(KeyUserName, "")
        }
        return userName*/
        return "quang"
    }

    fun setUserName(userName: String) {
        this.userName = userName
        sharedPreferences.edit().putString(KeyUserName, userName).apply()
    }

    fun getFcmToken(): String? {
        if (fcmToken == null) {
            fcmToken = sharedPreferences.getString(KeyFcm, "")
        }
        return fcmToken
    }

    fun setFcmToken(fcm: String?) {
        this.fcmToken = fcm
        sharedPreferences.edit().putString(KeyFcm, fcm).apply()
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
        this.location = location
    }

    fun getLocation() : Location{
        return location
    }
}

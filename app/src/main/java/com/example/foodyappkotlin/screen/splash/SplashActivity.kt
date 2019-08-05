package com.example.foodyappkotlin.screen.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.UserResponse
import com.example.foodyappkotlin.screen.login.LoginActivity
import com.example.foodyappkotlin.screen.main.MainActivity
import com.example.foodyappkotlin.screen.maps.MapsActivity
import com.google.android.gms.maps.model.LatLng
import dagger.android.AndroidInjection
import javax.inject.Inject


class SplashActivity : BaseActivity() {
    var location: Location? = null
    private val mWaitHandler = Handler()
    private var mLocationPermissionGranted: Boolean = false

    lateinit var mPresenter: SplashPresenter

    @Inject
    lateinit var foodyRepository: FoodyRepository

    @Inject
    lateinit var appSharedPreferences: AppSharedPreference

    companion object {
        private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.foodyappkotlin.R.layout.splash_activity)
        AndroidInjection.inject(this)
        getLocationPermission()
        mPresenter = SplashPresenter(foodyRepository, this)
//        delayTime()
    }

    override fun onResume() {
        super.onResume()
        if (appSharedPreferences.getToken() == "") {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (checkNetWorkEnabled()) {
                mPresenter.getUserLogin(appSharedPreferences.getToken()!!)
            } else {
                //ShowAlert
            }
        }
        delayTime()
    }

    private fun delayTime() {
        mWaitHandler.postDelayed({
            try {

            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }, 2000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            (MapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
            val locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (network_enabled) {
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                appSharedPreferences.setLocation(location!!)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun checkNetWorkEnabled(): Boolean {
        val locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return network_enabled
    }

    fun getUserSuccess(userResponse: UserResponse) {
        appSharedPreferences.setUser(userResponse)
        val intent = MainActivity.newInstance(this)
        startActivity(intent)
        finish()
    }

    fun getUserFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
    }
}

package com.example.reviewfoodkotlin.screen.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.common.BaseActivity
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.response.UserResponse
import com.example.reviewfoodkotlin.screen.login.LoginActivity
import com.example.reviewfoodkotlin.screen.main.MainActivity
import com.example.reviewfoodkotlin.screen.maps.MapsActivity
import com.google.android.gms.maps.model.LatLng
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_odau.*
import javax.inject.Inject
import android.net.NetworkInfo
import android.net.ConnectivityManager




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
        setContentView(com.example.reviewfoodkotlin.R.layout.splash_activity)
        AndroidInjection.inject(this)
        getLocationPermission()
        mPresenter = SplashPresenter(foodyRepository, this)
        delayTime()
    }

    private fun delayTime() {
        mWaitHandler.postDelayed({
            try {
                if (mLocationPermissionGranted) {
                    if (appSharedPreferences.getToken() == "") {
                        progressBar.visibility = View.GONE
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        if (isOnline()) {
                            mPresenter.getUserLogin(appSharedPreferences.getToken()!!)
                        } else {
                            //show message
                            showAlertMessage("Có lỗi","Vui lòng kiểm tra các kết nối mạng và thử lại")
                        }
                    }
                } else {
                    delayTime()
                }
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }, 5000)
    }

    @SuppressLint("MissingPermission")
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
                    val locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (network_enabled) {
                        location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        appSharedPreferences.setLocation(location!!)
                    }
                } else {
                    getLocationPermission()
                }
            }
        }
    }

    private fun getLocationPermission() {
        val defaultLocation = Location("")
        defaultLocation.latitude = 21.026047
        defaultLocation.longitude = 105.81267
        appSharedPreferences.setLocation(defaultLocation)
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

    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
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

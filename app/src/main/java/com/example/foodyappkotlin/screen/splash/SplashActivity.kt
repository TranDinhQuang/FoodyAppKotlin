package com.example.foodyappkotlin.screen.splash

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
import android.util.Log
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.screen.login.LoginActivity
import com.example.foodyappkotlin.screen.maps.MapsActivity
import com.google.android.gms.maps.model.LatLng


class SplashActivity : BaseActivity() {
    var location: Location? = null
    private val mWaitHandler = Handler()
    private var mLocationPermissionGranted: Boolean = false

    companion object {
        private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.foodyappkotlin.R.layout.splash_activity)
        getLocationPermission()
        delayTime()
    }

    private fun delayTime() {
        mWaitHandler.postDelayed({
            try {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }, 5000)  //
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        print("vao day nhes")
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
                if (location != null) {
                    val longitude = location!!.longitude
                    val latitude = location!!.latitude
                    Log.d("kiemtra", "$longitude " + latitude)
                }
            }

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
}
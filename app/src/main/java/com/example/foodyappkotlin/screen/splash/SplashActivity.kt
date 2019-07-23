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
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.login.LoginActivity
import com.example.foodyappkotlin.screen.maps.MapsActivity
import com.google.android.gms.maps.model.LatLng
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.item_odau.view.*
import kotlinx.android.synthetic.main.splash_activity.*
import javax.inject.Inject


class SplashActivity : BaseActivity() {
    var location: Location? = null
    private val mWaitHandler = Handler()
    private var mLocationPermissionGranted: Boolean = false

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
        delayTime()
    }

    override fun onResume() {
        super.onResume()
        appSharedPreferences.getUserName()
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
}

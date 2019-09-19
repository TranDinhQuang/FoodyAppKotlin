package com.example.reviewfoodkotlin.screen.main.fragment.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseFragment
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.di.module.GlideApp
import com.example.reviewfoodkotlin.screen.login.LoginActivity
import com.example.reviewfoodkotlin.screen.main.MainActivity
import com.example.reviewfoodkotlin.screen.maps.MapsActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import javax.inject.Inject

class ProfileFragment : BaseFragment() {
    private var mLocationPermissionGranted: Boolean = false
    var location: Location? = null

    @Inject
    lateinit var foodyRepository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var mActivity: MainActivity

    companion object {
        fun newInstance(): Fragment {
            val profileFragment = ProfileFragment()
            return profileFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_profile, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        mActivity.actionbarBack.visibility = View.GONE
    }

    private fun initData() {
        txt_logout.setOnClickListener {
            val intent = Intent(activityContext, LoginActivity::class.java)
            startActivity(intent)
            appSharedPreference.setToken("")
            mActivity.finish()
        }
        glideLoadImage(img_avatar_user, appSharedPreference.getUser().hinhanh)
        txt_user_name.text = appSharedPreference.getUser().tenhienthi
        txt_user.text = appSharedPreference.getUser().taikhoan
        updateLocation()
        if (appSharedPreference.getUser().permission == 2) {
            txt_permission.text = "Người dùng"
        } else {
            txt_permission.text = "Admin"
        }

        img_update_location.setOnClickListener {
            showAlertListerner(
                "Thông báo!",
                "Bạn muốn cập nhật lại vị trí của bạn?",
                DialogInterface.OnClickListener { p0, p1 -> getLocationPermission() })
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateLocation() {
        txt_address.text = getAddress(
            appSharedPreference.getLocation().latitude,
            appSharedPreference.getLocation().longitude
        )
        txt_latitude.text = "Latitude: ${appSharedPreference.getLocation().latitude}"
        txt_longitude.text = "Longitude: ${appSharedPreference.getLocation().longitude}"
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
                    val locManager =
                        mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val network_enabled =
                        locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (network_enabled) {
                        location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        appSharedPreference.setLocation(location!!)
                        updateLocation()
                    }
                } else {
                    getLocationPermission()
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                activityContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
            val locManager = mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (network_enabled) {
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                appSharedPreference.setLocation(location!!)
            } else {
                showAlertMessage("Có lỗi", "Vui lòng bật Định vị trí và thử lại")
            }
        } else {
            ActivityCompat.requestPermissions(
                activityContext,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    fun isOnline(): Boolean {
        val cm = mActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun glideLoadImage(img: ImageView, url: String) {
        GlideApp.with(activityContext)
            .load(url)
            .error(R.drawable.placeholder)
            .fitCenter()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    fun getAddress(latitude: Double, longitude: Double): String {
        var addressString = ""
        val geo = Geocoder(activityContext, Locale.getDefault())
        val addresses = geo.getFromLocation(latitude, longitude, 1)
        if (addresses.isEmpty()) {
        } else {
            if (addresses.size > 0) {
                addresses.forEach {
                    addressString = it.getAddressLine(0)
                    Log.d("kiemtra", "dia chi ${it.getAddressLine(0)}")
                }
            }
        }
        return addressString
    }
}
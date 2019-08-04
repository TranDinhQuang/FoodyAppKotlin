package com.example.foodyappkotlin.screen.maps

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.foodyappkotlin.common.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


class MapsActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var mMap: GoogleMap
    lateinit var mCameraPosition: CameraPosition

    // The entry points to the Places API.
    private var mGeoDataClient: GeoDataClient? = null
    private var mLocationPermissionGranted: Boolean = false
    private var mPlaceDetectionClient: PlaceDetectionClient? = null
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null


    companion object {
        val TAG: String = "MAPS_ACTIVITY"
        val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        val DEFAULT_ZOOM = 15
        private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.foodyappkotlin.R.layout.activity_maps)
        getLocationPermission()
        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.foodyappkotlin.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()
    }

    private fun getLocationPermission() {
        /*
            * Request location permission, so that we can get the location of the
            * device. The result of the permission request is handled by a callback,
            * onRequestPermissionsResult.
            */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            (PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true)
                mMap.getUiSettings().isMyLocationButtonEnabled = true
            } else {
                mMap.setMyLocationEnabled(false)
                mMap.getUiSettings().isMyLocationButtonEnabled = false
                mLastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            print("Exception: %s ${e.message}")
        }
    }

    fun getDeviceLocation() {/*
      * Get the best and most recent location of the device, which may be null in rare
      * cases when a location is not available.
      */
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.result
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude
                                ), MapsActivity.DEFAULT_ZOOM.toFloat()
                            )
                        )
                    } else {

                        mMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(
                                    mDefaultLocation,
                                    MapsActivity.DEFAULT_ZOOM.toFloat()
                                )
                        )
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            } else {
            }
        } catch (e: SecurityException) {
            print(e.message)
        }
    }
}

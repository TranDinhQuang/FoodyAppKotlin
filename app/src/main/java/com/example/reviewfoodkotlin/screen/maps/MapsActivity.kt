package com.example.reviewfoodkotlin.screen.maps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.reviewfoodkotlin.common.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*


class MapsActivity : BaseActivity(), OnMapReadyCallback {
    var address = ""
    lateinit var mMap: GoogleMap
    lateinit var mCameraPosition: CameraPosition


    var builder = PlacePicker.IntentBuilder()

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

        val PLACE_AUTOCOMPLETE_REQUEST_CODE = 0
        private val mDefaultLocation = LatLng(21.008513, 105.846314)

        val PLACE_PICKER_REQUEST = 2

        var AUTOCOMPLETE_REQUEST_CODE = 3

        fun newInstance(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.reviewfoodkotlin.R.layout.activity_maps)
        getLocationPermission()

        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.reviewfoodkotlin.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()
//        callPlaceAutocompleteActivityIntent()

        mMap.setOnMapClickListener {
            // Creating a marker
            val markerOptions = MarkerOptions()

            // Setting the position for the marker
            markerOptions.position(it)

            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title("${it.latitude} - ${it.longitude}")
            mLastKnownLocation!!.latitude = it.latitude
            mLastKnownLocation!!.longitude = it.longitude
            address = getAddress(it.latitude, it.longitude)
            txt_show_pick_location.text = address
            // Clears the previously touched position
            googleMap.clear()

            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))

            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions)
        }

        btn_ok.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", address)
            returnIntent.putExtra("latitude", mLastKnownLocation!!.latitude)
            returnIntent.putExtra("longitude", mLastKnownLocation!!.longitude)
            Log.d("kiemtra","${mLastKnownLocation!!.latitude} - ${mLastKnownLocation!!.longitude}")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun getLocationPermission() {
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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            print("Exception: %s ${e.message}")
        }
    }

    fun getDeviceLocation() {
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
                        val markerOptions = MarkerOptions()

                        // Setting the position for the marker
                        markerOptions.position(
                            LatLng(
                                mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude
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

/*    private fun callPlaceAutocompleteActivityIntent() {
        try {
            val intent =
                PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
        } catch (e: GooglePlayServicesRepairableException) {
            print(e.message)
        }

    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = PlaceAutocomplete.getPlace(this, data)
                    Log.d(TAG, "Place:$place")
                }
                PlaceAutocomplete.RESULT_ERROR -> {
                    val status = PlaceAutocomplete.getStatus(this, data)
                    Log.d(TAG, status.statusMessage)
                }
                RESULT_CANCELED -> {
                    Log.d(TAG, "Cencel")
                }
            }
        }
    }

    fun getAddress(latitude: Double, longitude: Double): String {
        var addressString = ""
        val geo = Geocoder(applicationContext, Locale.getDefault())
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

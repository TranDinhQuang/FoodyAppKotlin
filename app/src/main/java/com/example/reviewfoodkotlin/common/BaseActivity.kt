package com.example.reviewfoodkotlin.common

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.example.reviewfoodkotlin.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private lateinit var mFragmentTransition : FragmentTransaction
    private var mFragmentManager = supportFragmentManager

    @Inject
    protected lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        super.onCreate(savedInstanceState)
        //LocalBroadcastManager.getInstance(this).registerReceiver(logoutReceiver, IntentFilter("LOGOUT"))
    }

    override fun onDestroy() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutReceiver)
        super.onDestroy()
        Log.d("APPFLOW", " $localClassName onDestroy")
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }


    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    fun popFragment() {
        mFragmentManager.popBackStack()
    }

    fun pushFragment(id: Int, fragment: Fragment) {
        mFragmentTransition = supportFragmentManager.beginTransaction()
        mFragmentTransition.replace(id, fragment)
        mFragmentTransition.addToBackStack(null)
        mFragmentTransition.commitAllowingStateLoss()
    }

    fun pushFragmentWithoutBackStack(id: Int, fragment: Fragment) {
        mFragmentTransition = supportFragmentManager.beginTransaction()
        mFragmentTransition.replace(id, fragment)
        mFragmentTransition.commitAllowingStateLoss()
    }

    fun showAlertMessage(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Đồng ý", null).show()
    }


    fun showAlertListerner(
        title: String,
        message: String,
        listerner: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Đồng ý", listerner).setNegativeButton("Hủy bỏ", null).show()
    }

    fun showAlertListernerOneClick(
        title: String,
        message: String,
        txtButton: String,
        listerner: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(txtButton, listerner).show()
    }

    fun showPermissionDialog() {
        Dexter.withActivity(this).withPermissions().withListener(
            object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
        ).withErrorListener { showSettingsDialog() }
            .onSameThread()
            .check()
    }


    fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.message_need_permission))
        builder.setMessage(getString(R.string.message_grant_permission))
        builder.setPositiveButton(getString(R.string.label_setting)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun convertDpToPx(dp: Int): Int {
        return Math.round(dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}
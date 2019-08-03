package com.example.foodyappkotlin.common

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
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

    fun showAlertMessage(title : String,message : String){
        AlertDialog.Builder(applicationContext)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Đồng ý", null).show()
    }
}
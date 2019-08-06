package com.example.foodyappkotlin.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import javax.inject.Named
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager


abstract class BaseFragment: Fragment(), HasSupportFragmentInjector {

    @Inject
    @Named(BaseActivityModule.ACTIVITY_CONTEXT)
    protected lateinit var activityContext: AppCompatActivity

    @Inject
    protected lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun onAttach(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(activity)
    }

    override fun onAttach(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }

    fun showKeyBoard(){

    }

    fun hideKeyBoard(){

    }

    fun showAlertListerner(title : String,message : String,listerner : DialogInterface.OnClickListener ){
        AlertDialog.Builder(activityContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Đồng ý", listerner).setNegativeButton("Hủy bỏ", null).show()
    }


    fun showAlertMessage(title : String,message : String){
        AlertDialog.Builder(activityContext)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Đồng ý", null).show()
    }
}


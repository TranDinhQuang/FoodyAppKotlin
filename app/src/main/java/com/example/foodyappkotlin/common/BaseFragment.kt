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
}

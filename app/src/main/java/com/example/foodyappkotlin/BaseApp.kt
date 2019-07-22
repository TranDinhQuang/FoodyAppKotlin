package com.example.foodyappkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.Service
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.example.foodyappkotlin.di.component.DaggerApplicationComponent
import com.example.foodyappkotlin.di.module.NetworkModule
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

@SuppressLint("Registered")
class BaseApp : MultiDexApplication(), HasActivityInjector, HasFragmentInjector, HasSupportFragmentInjector,
    HasServiceInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var dispatchingFragmentV4Injector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()
        // initialize Dagger
        DaggerApplicationComponent.builder().application(this).build().inject(this)
    }

    // this is required to setup Dagger2 for Activity
    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingFragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return dispatchingFragmentV4Injector
    }

    // this is required to setup Dagger2 for Service
    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }
}

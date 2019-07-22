package com.example.foodyappkotlin.di.module

import com.example.foodyappkotlin.di.scope.ActivityScoped
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.detail.DetailEatingModule
import com.example.foodyappkotlin.screen.main.MainActivity
import com.example.foodyappkotlin.screen.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [(DetailEatingModule::class)])
    abstract fun detailEatingActivity(): DetailEatingActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity
}

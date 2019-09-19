package com.example.reviewfoodkotlin.di.module

import com.example.reviewfoodkotlin.di.scope.ActivityScoped
import com.example.reviewfoodkotlin.screen.detail.DetailEatingActivity
import com.example.reviewfoodkotlin.screen.detail.DetailEatingModule
import com.example.reviewfoodkotlin.screen.login.LoginActivity
import com.example.reviewfoodkotlin.screen.main.MainActivity
import com.example.reviewfoodkotlin.screen.main.MainModule
import com.example.reviewfoodkotlin.screen.menu.MenuActivity
import com.example.reviewfoodkotlin.screen.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [(DetailEatingModule::class)])
    abstract fun detailEatingActivity(): DetailEatingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(MainModule::class)])
    abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun menuActivity() : MenuActivity
}

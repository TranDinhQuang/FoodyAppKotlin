package com.example.reviewfoodkotlin.screen.main

import android.support.v7.app.AppCompatActivity
import com.example.reviewfoodkotlin.common.BaseActivityModule
import com.example.reviewfoodkotlin.di.scope.ActivityScoped
import com.example.reviewfoodkotlin.di.scope.FragmentScoped
import com.example.reviewfoodkotlin.screen.main.fragment.ODauFragment
import com.example.reviewfoodkotlin.screen.main.fragment.home.HomeFragment
import com.example.reviewfoodkotlin.screen.main.fragment.myself.ChangingQuanAnFragment
import com.example.reviewfoodkotlin.screen.main.fragment.myself.PostQuanAnFragment
import com.example.reviewfoodkotlin.screen.main.fragment.myself.QuanAnCuaToiFragment
import com.example.reviewfoodkotlin.screen.main.fragment.profile.ProfileFragment
import com.example.reviewfoodkotlin.screen.main.fragment.search.SearchFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [(BaseActivityModule::class)])
abstract class MainModule {

    @Binds
    @ActivityScoped
    abstract fun appCompatActivity(mainActivity: MainActivity): AppCompatActivity

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun oDauFragment(): ODauFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun quanAnCuaToiFragment(): QuanAnCuaToiFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun searchFragment(): SearchFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun postQuanAnFragment(): PostQuanAnFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun changingQuanAnFragment(): ChangingQuanAnFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun profileFragment(): ProfileFragment
}

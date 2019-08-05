package com.example.foodyappkotlin.screen.main

import android.support.v7.app.AppCompatActivity
import com.example.foodyappkotlin.common.BaseActivityModule
import com.example.foodyappkotlin.di.scope.ActivityScoped
import com.example.foodyappkotlin.di.scope.FragmentScoped
import com.example.foodyappkotlin.screen.main.fragment.home.HomeFragment
import com.example.foodyappkotlin.screen.main.fragment.myself.QuanAnCuaToiFragment
import com.example.foodyappkotlin.screen.main.fragment.search.SearchFragment
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
    internal abstract fun quanAnCuaToiFragment(): QuanAnCuaToiFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun searchFragment(): SearchFragment
}

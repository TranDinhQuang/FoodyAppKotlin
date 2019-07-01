package com.example.foodyappkotlin.screen.detail

import android.support.v7.app.AppCompatActivity
import com.example.foodyappkotlin.common.BaseActivityModule
import com.example.foodyappkotlin.di.scope.ActivityScoped
import com.example.foodyappkotlin.di.scope.FragmentScoped
import com.example.foodyappkotlin.screen.detail.fragment_overview.OverviewFragment
import com.example.foodyappkotlin.screen.detail.fragment_overview.OverviewModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = arrayOf(BaseActivityModule::class))
abstract class DetailEatingModule {
    @Binds
    @ActivityScoped
    abstract fun appCompatActivity(detailEatingActivity : DetailEatingActivity): AppCompatActivity

    @FragmentScoped
    @ContributesAndroidInjector(modules = [(OverviewModule::class)])
    abstract fun overviewFragment(): OverviewFragment
}

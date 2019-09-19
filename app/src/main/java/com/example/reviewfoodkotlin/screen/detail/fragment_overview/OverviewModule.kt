package com.example.reviewfoodkotlin.screen.detail.fragment_overview

import com.example.reviewfoodkotlin.common.BaseFragmentModule
import com.example.reviewfoodkotlin.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module

@Module(includes = [(BaseFragmentModule::class)])
abstract class OverviewModule{
    @FragmentScoped
    @Binds
    abstract fun bindPresenter(overviewPresenter: OverviewPresenter): OverviewInterface.Presenter
}

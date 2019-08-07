package com.example.foodyappkotlin.screen.detail.fragment_overview

import com.example.foodyappkotlin.common.BaseFragmentModule
import com.example.foodyappkotlin.di.scope.FragmentScoped
import com.example.foodyappkotlin.screen.detail.fragment_post.PostCommentInterface
import com.example.foodyappkotlin.screen.detail.fragment_post.PostCommentPresenter
import dagger.Binds
import dagger.Module

@Module(includes = [(BaseFragmentModule::class)])
abstract class OverviewModule{
    @FragmentScoped
    @Binds
    abstract fun bindPresenter(overviewPresenter: OverviewPresenter): OverviewInterface.Presenter
}

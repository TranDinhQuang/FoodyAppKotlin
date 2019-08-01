package com.example.foodyappkotlin.screen.detail.fragment_post

import com.example.foodyappkotlin.common.BaseFragmentModule
import com.example.foodyappkotlin.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module

@Module(includes = [(BaseFragmentModule::class)])
abstract class PostCommentModule {

    @FragmentScoped
    @Binds
    abstract fun bindPresenter(postCommentPresenter: PostCommentPresenter): PostCommentInterface.Presenter
}

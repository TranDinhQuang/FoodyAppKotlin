package com.example.reviewfoodkotlin.screen.detail.fragment_post

import com.example.reviewfoodkotlin.common.BaseFragmentModule
import com.example.reviewfoodkotlin.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module

@Module(includes = [(BaseFragmentModule::class)])
abstract class PostCommentModule {

    @FragmentScoped
    @Binds
    abstract fun bindPresenter(postCommentPresenter: PostCommentPresenter): PostCommentInterface.Presenter
}

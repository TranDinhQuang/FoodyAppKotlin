package com.example.reviewfoodkotlin.screen.detail

import android.support.v7.app.AppCompatActivity
import com.example.reviewfoodkotlin.common.BaseActivityModule
import com.example.reviewfoodkotlin.di.scope.ActivityScoped
import com.example.reviewfoodkotlin.di.scope.FragmentScoped
import com.example.reviewfoodkotlin.screen.detail.fragment_comments.FragmentComments
import com.example.reviewfoodkotlin.screen.detail.fragment_detail_comment.FragmentDetailComment
import com.example.reviewfoodkotlin.screen.detail.fragment_overview.OverviewFragment
import com.example.reviewfoodkotlin.screen.detail.fragment_overview.OverviewModule
import com.example.reviewfoodkotlin.screen.detail.fragment_post.ChangingCommentFragment
import com.example.reviewfoodkotlin.screen.detail.fragment_post.PostCommentFragment
import com.example.reviewfoodkotlin.screen.detail.fragment_post.PostCommentModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [(BaseActivityModule::class)])
abstract class DetailEatingModule {

    @Binds
    @ActivityScoped
    abstract fun appCompatActivity(detailEatingActivity : DetailEatingActivity): AppCompatActivity

    @FragmentScoped
    @ContributesAndroidInjector(modules = [(OverviewModule::class)])
    abstract fun overViewFragment(): OverviewFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [(PostCommentModule::class)])
    internal abstract fun postCommentFragment(): PostCommentFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [(PostCommentModule::class)])
    internal abstract fun changingCommentFragment(): ChangingCommentFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun commentsFragment(): FragmentComments

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun detailComment(): FragmentDetailComment
}

/*
    @ContributesAndroidInjector
    This is the easiest way of injecting dependency in our Activity.*/
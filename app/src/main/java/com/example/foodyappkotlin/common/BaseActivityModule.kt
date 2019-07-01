package com.example.foodyappkotlin.common

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.example.foodyappkotlin.di.scope.ActivityScoped
import dagger.Binds
import dagger.Module
import javax.inject.Named


@Module
abstract class BaseActivityModule {

    companion object {
        const val ACTIVITY_CONTEXT = "activity_context"
    }

    @Binds
    @ActivityScoped
    @Named(ACTIVITY_CONTEXT)
    internal abstract fun context(activity: AppCompatActivity): Context

    @Binds
    @ActivityScoped
    @Named(ACTIVITY_CONTEXT)
    internal abstract fun activity(appCompatActivity: AppCompatActivity): Activity

    @Binds
    @ActivityScoped
    @Named(ACTIVITY_CONTEXT)
    internal abstract fun activityContext(@Named(ACTIVITY_CONTEXT) activity: AppCompatActivity): AppCompatActivity

}

package com.example.reviewfoodkotlin.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.BaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(app: BaseApp): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideApplication(baseApp: BaseApp): Application {
        return baseApp
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context : Context) :
            SharedPreferences {
        return context.getSharedPreferences("App", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppSharedPreference(preferences: SharedPreferences): AppSharedPreference {
        return AppSharedPreference(preferences)
    }
}

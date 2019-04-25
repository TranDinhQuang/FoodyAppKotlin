package com.example.foodyappkotlin.di.module

import android.app.Application
import com.example.foodyappkotlin.BaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp){
    @Provides
    @Singleton
    fun provideApplication(baseApp: BaseApp): Application {
        return baseApp
    }
}
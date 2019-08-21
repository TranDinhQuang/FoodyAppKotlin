package com.example.reviewfoodkotlin.di.component

import com.example.reviewfoodkotlin.BaseApp
import com.example.reviewfoodkotlin.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ApplicationModule::class), (ActivityBindingModule::class),
        (FragmentBindingModule::class), (RepositoryBindingModule::class), (AppGlideModule::class)]
)

interface ApplicationComponent : AndroidInjector<BaseApp> {
    @Component.Builder
    interface Builder {
        // provide Application instance into DI
        @BindsInstance
        fun application(application: BaseApp): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}

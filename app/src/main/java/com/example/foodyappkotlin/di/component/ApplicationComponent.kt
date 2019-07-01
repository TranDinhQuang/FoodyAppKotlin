package com.example.foodyappkotlin.di.component

import com.example.foodyappkotlin.BaseApp
import com.example.foodyappkotlin.di.module.ActivityBindingModule
import com.example.foodyappkotlin.di.module.AppGlideModule
import com.example.foodyappkotlin.di.module.FragmentBindingModule
import com.example.foodyappkotlin.di.module.RepositoryBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ActivityBindingModule::class),
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

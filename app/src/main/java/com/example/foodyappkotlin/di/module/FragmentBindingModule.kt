package com.example.foodyappkotlin.di.module

import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.common.BaseFragmentModule
import com.example.foodyappkotlin.di.scope.FragmentScoped
import com.example.foodyappkotlin.screen.main.fragment.ODauFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun oDauFragment(): ODauFragment
}

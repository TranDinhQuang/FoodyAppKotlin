package com.example.foodyappkotlin.common

import android.support.v4.app.Fragment
import com.example.foodyappkotlin.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class BaseFragmentModule {
    companion object {
        const val FRAGMENT_CONTEXT = "fragment_context"
    }

    @Binds
    @FragmentScoped
    @Named(FRAGMENT_CONTEXT)
    internal abstract fun fragment(fragment: BaseFragment): Fragment

    @Binds
    @FragmentScoped
    @Named(FRAGMENT_CONTEXT)
    internal abstract fun fragmentContext(@Named(FRAGMENT_CONTEXT) fragment: BaseFragment): BaseFragment
}

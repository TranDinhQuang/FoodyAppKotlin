package com.example.foodyappkotlin.di.module

import com.example.foodyappkotlin.di.scope.FragmentScoped
import com.example.foodyappkotlin.screen.detail.fragment_post.PostCommentFragment
import com.example.foodyappkotlin.screen.main.fragment.ODauFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun oDauFragment(): ODauFragment

/*    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun postCommentFragment(): PostCommentFragment*/
}

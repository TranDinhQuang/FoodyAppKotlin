package com.example.foodyappkotlin.di.module

import com.example.foodyappkotlin.data.repository.FoodyRepositoryModule
import dagger.Module

@Module(includes = [(FoodyRepositoryModule::class)])
abstract class RepositoryBindingModule

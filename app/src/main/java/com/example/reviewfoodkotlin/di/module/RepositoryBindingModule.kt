package com.example.reviewfoodkotlin.di.module

import com.example.reviewfoodkotlin.data.repository.FoodyRepositoryModule
import dagger.Module

@Module(includes = [(FoodyRepositoryModule::class)])
abstract class RepositoryBindingModule

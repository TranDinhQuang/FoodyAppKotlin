package com.example.reviewfoodkotlin.data.repository

import com.example.reviewfoodkotlin.data.source.FoodyDataSource
import com.example.reviewfoodkotlin.data.source.remote.FoodyRemoteDataSource
import com.haipq.miniweather.data.source.Remote
import dagger.Module
import dagger.Provides

@Module
class FoodyRepositoryModule {
    @Provides
    @Remote
    fun providerRemoteDataSource(): FoodyDataSource.Remote {
        return FoodyRemoteDataSource()
    }
}

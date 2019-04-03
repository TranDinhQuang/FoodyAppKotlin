package com.example.foodyappkotlin.data.repository

import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.data.source.remote.FoodyRemoteDataSource
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

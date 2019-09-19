package com.example.reviewfoodkotlin.data.source.retrofit

import android.app.Application
import com.example.reviewfoodkotlin.util.Constants
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitConfig {
    private val QUERRY_PARAMETER_API_KEY = "api_key"
    private val CONNECTION_TIMEOUT = 60
    private val CACHE_SIZE = 10 * 1024 * 1024 // 10 MiB;


    /*fun <T> createService(application: Application, endPoint: String, serviceClass: Class<T>): T {
        return createService(application, endPoint, serviceClass, initInterception())
    }*/

    fun <T> createService(
        application: Application, endPoint: String, serviceClass: Class<T>
    ): T {
        val httpClientBuilder = OkHttpClient.Builder()

        val cacheSize = CACHE_SIZE
        httpClientBuilder.cache(Cache(application.cacheDir, cacheSize.toLong()))
        /*if (interceptor != null) {
            httpClientBuilder.addInterceptor(interceptor)
        }*/
        httpClientBuilder.readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        val builder = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.client(httpClientBuilder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(serviceClass)
    }

    /*fun initInterception(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()

            val newUrl = originalHttpUrl.newBuilder()
                .addQueryParameter(QUERRY_PARAMETER_API_KEY, "API_KEY")
                .build()

            val customBuilder = originalRequest.newBuilder().url(newUrl)
            val customRequest = customBuilder.build()

            chain.proceed(customRequest)
        }
    }*/
}

package com.dave.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {
    private val okHttpClient: OkHttpClient
    private val httpLogging: HttpLoggingInterceptor

    init {
        httpLogging = provideLoggingInterceptor()
        okHttpClient = provideOkHttpClient(httpLogging)
    }

    fun <T : Any> provideApi(apiInterface: T,
                             baseUrl: String,
                             isDebug: Boolean = true): T {

        httpLogging.level = if (isDebug)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(apiInterface::class.java)
    }


    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient().newBuilder()
                    .run { addInterceptor(interceptor) }
                    .build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
}

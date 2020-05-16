package com.android.friendycar.model.remote

import android.content.Context

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val retrofitClient: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())

        .client(okHttpClient)
        .build()
}

private const val BASE_URL = "https://wainnakel.com"


val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request: Request
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Content-Type", "application/json")
            request = requestBuilder.build()
            chain.proceed(request)
        }

        .addInterceptor(httpLoggingInterceptor)

        .build()
}

val httpLoggingInterceptor by lazy {
    return@lazy HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

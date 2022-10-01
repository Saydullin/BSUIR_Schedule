package com.bsuir.bsuirschedule.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val baseUrl = "https://iis.bsuir.by/api/v1/"

    fun getInstance(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("User-Agent", "BSUIRScheduleApp")
                    .addHeader("Keep-Alive", "50")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Language", "ru-RU")
                    .build()
                chain.proceed(request)
            }
            .build()

        return  Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}

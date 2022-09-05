package com.example.testapplication.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ImageRepo {
    companion object {
        private val builder = OkHttpClient.Builder()
        private val retrofit = Retrofit.Builder().baseUrl("https://dev.bedaru.io/v1/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).client((builder.build())).build()

        val service= retrofit.create(ImageApi::class.java)
    }
}
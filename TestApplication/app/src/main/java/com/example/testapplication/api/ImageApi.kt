package com.example.testapplication.api

import com.example.testapplication.data.ImageApiData
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {
    @Multipart
    @POST("image")
    suspend fun postImageAsync(
        @Header("Authorization") token:String,
        @Part image:MultipartBody.Part
    ):Response<ImageApiData>
}
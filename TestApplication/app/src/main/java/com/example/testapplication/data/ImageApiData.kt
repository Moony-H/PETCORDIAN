package com.example.testapplication.data

import com.google.gson.annotations.SerializedName

data class ImageApiData(
    @SerializedName("src")
    val src:String
)
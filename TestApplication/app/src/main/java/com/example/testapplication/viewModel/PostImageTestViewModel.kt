package com.example.testapplication.viewModel

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.Utils
import com.example.testapplication.api.ImageRepo
import com.example.testapplication.data.BitmapRequestBody
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.util.*

class PostImageTestViewModel:ViewModel() {

    private val _imageUri=MutableLiveData<Uri>()
    val imageUri:LiveData<Uri>
        get()=_imageUri

    private val _photoUri=MutableLiveData<Uri>()
    val photoUri:LiveData<Uri>
        get() = _photoUri

    private var _photoFile: File?=null
    val photoFile:File?
        get()=_photoFile


    fun postImage(uri:Uri) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).toString(),
                BitmapRequestBody(uri,_photoFile!!)
            )
            val response = ImageRepo.service.postImageAsync(Utils.token, multipartBody)
            when (response.code()) {
                200 -> {
                    Log.d(
                        this@PostImageTestViewModel::class.java.name,
                        "testing code: 200  success image src:${response.body()?.src}"
                    )
                    _imageUri.postValue(Uri.parse(response.body()?.src))
                }
                400 -> {
                    Log.d(
                        this@PostImageTestViewModel::class.java.name,
                        "testing code: 400  params error"
                    )
                }
                else -> {
                    Log.d(
                        this@PostImageTestViewModel::class.java.name,
                        "testing code: ${response.code()} error"
                    )
                }
            }


        }
    }

    fun setPhotoUri(uri: Uri){
        _photoUri.value=uri
    }

    fun setPhotoFile(file: File){
        _photoFile=file
    }

    private val coroutineExceptionHandler= CoroutineExceptionHandler{_, throwable->
        throwable.printStackTrace()

    }

}
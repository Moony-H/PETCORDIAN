package com.example.testapplication.viewModel

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.TestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MultiPickerViewModel: ViewModel() {

    private val _image = MutableLiveData<List<Bitmap>>()
    val image: LiveData<List<Bitmap>>
        get() = _image

    private val _imageUri=MutableLiveData<ArrayList<Uri>>()
    val imageUri:LiveData<ArrayList<Uri>>
        get() = _imageUri


    fun setSelectedImagesWithBitmap(images: List<Bitmap>) {
        _image.value = images
    }

    fun setSelectedImagesWithUri(images: ArrayList<Uri>) {

        viewModelScope.launch(Dispatchers.IO) {
            _imageUri.postValue(images)
            val imageList= mutableListOf<Bitmap>()
            images.forEach { imageUri ->
                val bitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        TestApplication.getAppContext().contentResolver,
                        imageUri
                    )
                )
                imageList.add(bitmap)
            }
            withContext(Dispatchers.Main){
                _image.value=imageList
            }


        }
    }

}
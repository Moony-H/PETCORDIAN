package com.example.testapplication.data

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.example.testapplication.ImageResizer
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.IOException

class BitmapRequestBody(private val uri: Uri,private val photoFile: File):RequestBody() {
    override fun contentType(): MediaType="image/jpeg".toMediaType()

    override fun writeTo(sink: BufferedSink) {
        var bitmap=ImageResizer.resizeBitmap(uri)
        val imageRotation=getImageRotation()
        if(imageRotation!=0)
            bitmap= bitmap?.let { getBitmapRotatedByDegree(it,imageRotation) }
        bitmap?.compress(Bitmap.CompressFormat.JPEG,100,sink.outputStream())

    }

    private fun getImageRotation():Int{
        var exif: ExifInterface? =null
        var rotation=0
        try{
            //이미지의 메타 데이터를 가져온다
            exif= ExifInterface(photoFile.absoluteFile.path)

            //메타 데이터 중에 회전 각도를 가져온다(실제 각도는 아님.)
            rotation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        }catch (e:IOException){
            e.printStackTrace()
        }

        //메타 데이터 가져오기가 성공
        exif?.let {
            val degree=exifToDegrees(rotation)
            Log.d("BitmapRequestBody","exif degree is $degree")
            return degree
        }?:run {
            Log.d("BitmapRequestBody","exif is null. return 0")
            return 0
        }
    }

    private fun exifToDegrees(rotation:Int):Int{
        return when(rotation){
            ExifInterface.ORIENTATION_ROTATE_90-> 90
            ExifInterface.ORIENTATION_ROTATE_180-> 180
            ExifInterface.ORIENTATION_ROTATE_270-> 270
            else-> 0
        }
    }

    private fun getBitmapRotatedByDegree(bitmap: Bitmap,rotationDegree:Int):Bitmap{
        val matrix=Matrix()
        matrix.preRotate(rotationDegree.toFloat())
        return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }
}
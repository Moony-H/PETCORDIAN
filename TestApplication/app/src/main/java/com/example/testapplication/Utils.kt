package com.example.testapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class Utils {

    companion object {

        val token="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY2MTg0Nzk5OSwiZXhwIjoxNjYyNzExOTk5fQ.2JMzhcJS1r2d7tN45EsUMEJ8WkSfKBs-G3OLlQr8h4w"
        fun requestPermission(
            fragment: Fragment,
            activityResultLauncher: ActivityResultLauncher<String>,
            permission: String
        ) {

            if (ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activityResultLauncher.launch(permission)
            }
        }

        fun openAlertDialog(
            context:Context,
            title: String,
            message: String,
            positiveButtonText: String,
            negativeButtonText: String,
            positiveButtonCallback: (DialogInterface, which: Int) -> Unit
        ) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText){dialog,which->
                    positiveButtonCallback(dialog,which)
                }
                .setNegativeButton(negativeButtonText,{_,_->})
                .create()
                .show()

        }
        fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
            //create a file to write bitmap data
            var file: File? = null
            return try {
                file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
                file.createNewFile()

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
                val bitmapdata = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                file
            } catch (e: Exception) {
                e.printStackTrace()
                file // it will return null
            }
        }

        fun persistImage(bitmap: Bitmap,name:String):File{
            val filesDir=TestApplication.getAppContext().cacheDir
            val imageFile=File(filesDir, "$name.jpg")
            try {
                val os=FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,os)
                os.flush()
                os.close()
            }catch (e:java.lang.Exception){
                Log.e(this::class.java.name,"Error writing bitmap",e)
            }

            return imageFile
        }


    }
}
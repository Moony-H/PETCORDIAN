package com.example.testapplication

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.testapplication.custom.ScalingLogic

class ImageResizer {

    companion object{


        fun resizeImage(bitmap: Bitmap, width: Int, height: Int): Bitmap {
            return Bitmap.createScaledBitmap(bitmap, width, height, true)
        }


        fun resizeBitmap(uri: Uri, resize: Int): Bitmap? {
            val options = BitmapFactory.Options()
            BitmapFactory.decodeStream(
                TestApplication.getAppContext().contentResolver.openInputStream(
                    uri
                ), null, options
            )

            var width = options.outWidth
            var height = options.outHeight

            var sampleSize = 1

            while (width / 2 >= resize && height / 2 >= resize) {
                width /= 2
                height /= 2
                sampleSize *= 2
            }


            options.inSampleSize = sampleSize


            return BitmapFactory.decodeStream(
                TestApplication.getAppContext().contentResolver.openInputStream(
                    uri
                ), null, options
            )
        }

        private fun calculateSampleSize(
            srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
            scalingLogic: ScalingLogic
        ): Int {
            if (scalingLogic == ScalingLogic.FIT) {
                //사진의 width 에 맞춘 비율을 구한다 (dstAspect 는 1)
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
                Log.d("ImageResizer","srcAspect: $srcAspect")
                Log.d("ImageResizer","dstAspect: $dstAspect")

                //dstAspect는 무조건 1.
                //src는 width 기준의 비율. srcAspect가 1보다 크다면 width가 더 큰것.
                return if (srcAspect > dstAspect) {
                    //width 가 더 기니까 현재 width 와 목표 width 의 비율을 구한 다음 샘플 사이즈로 사용
                    srcWidth / dstWidth
                } else {
                    //height 가 더 기니까 현재 width 와 목표 height 의 비율을 구한 다음 샘플 사이즈로 사용
                    srcHeight / dstHeight
                }
            } else {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

                return if (srcAspect > dstAspect) {
                    srcHeight / dstHeight
                } else {
                    srcWidth / dstWidth
                }
            }
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int{
            // Raw height and width of image
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }


        fun resizeBitmap(
            uri: Uri,
        ): Bitmap? {
            // First decode with inJustDecodeBounds=true to check dimensions
            return BitmapFactory.Options().run {
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(TestApplication.getAppContext().contentResolver.openInputStream(
                    uri
                ), null, this)

                // Calculate inSampleSize
                val originHeight=this.outHeight
                val originWidth=this.outWidth

                Log.d("ImageResizer","origin width: $originWidth origin height: $originHeight")

                inSampleSize = calculateSampleSize(this.outWidth,this.outHeight,1280,1280,ScalingLogic.FIT)
                Log.d("ImageResizer","in sample size is $inSampleSize")
                // Decode bitmap with inSampleSize set
                inJustDecodeBounds = false

                val bitmap=BitmapFactory.decodeStream(TestApplication.getAppContext().contentResolver.openInputStream(
                    uri
                ), null, this)
                bitmap?.let {
                    Log.d("testing","result width: ${bitmap.width} result height: ${bitmap.height}")
                }
                bitmap
            }
        }
    }

}

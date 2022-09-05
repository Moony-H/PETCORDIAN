package com.example.testapplication.view

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapplication.ImageResizer
import com.example.testapplication.databinding.FragmentResizeTestBinding

class ResizeTestFragment:Fragment() {




    private var _binding:FragmentResizeTestBinding?=null
    private val binding:FragmentResizeTestBinding
        get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentResizeTestBinding.inflate(layoutInflater,container,false)

        val uri=Uri.parse("content://media/external/images/media/41")


        //val bitmap= ImageResizer.resizeBitmap(uri, 50)
        //val bitmap =ImageResizer.resizeBitmap(uri,50,50)
        val bitmap =ImageResizer.resizeImage(BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri)),50,50)
        val originBitmap=BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))


        bitmap?.let {
            Log.d("testing resized width","${it.width}")
            Log.d("testing resized height","${it.height}")
        }

        originBitmap?.let{
            Log.d("testing origin width","${it.width}")
            Log.d("testing origin height","${it.height}")
        }


        binding.fragmentResizeTestResized.setImageBitmap(bitmap)
        binding.fragmentResizeTestOrigin.setImageBitmap(originBitmap)



        return binding.root
    }
}
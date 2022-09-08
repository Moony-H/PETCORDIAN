package com.example.testapplication.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.testapplication.StaggeredAdapter
import com.example.testapplication.data.StaggeredItemData
import com.example.testapplication.databinding.FragmentScrollBinding
import com.google.android.material.appbar.AppBarLayout
import java.lang.Math.abs

class ScrollFragment:Fragment() {


    private var _binding:FragmentScrollBinding?=null
    private val binding:FragmentScrollBinding
        get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentScrollBinding.inflate(layoutInflater,container,false)
        binding.fragmentScrollStickyScrollView.setHeaders(arrayListOf(binding.fragmentScrollTempHeader))

        val uriArray=Array(8){
            val uri=Uri.parse("android.resource://com.example.testapplication/drawable/stagg_test${it+1}")
            val stream=requireContext().contentResolver.openInputStream(uri)
            uri
        }
        val list=List(8){
            StaggeredItemData("강아지${it+1}",uriArray[it],"")
        }

//        val list=listOf(
//            StaggeredItemData("강아지1", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test1.jpeg"),""),
//            StaggeredItemData("강아지2", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test2.jpeg"),""),
//            StaggeredItemData("강아지3", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test3.jpeg"),""),
//            StaggeredItemData("강아지4", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test4.jpeg"),""),
//            StaggeredItemData("강아지5", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test5.jpeg"),""),
//            StaggeredItemData("강아지6", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test6.jpeg"),""),
//            StaggeredItemData("강아지7", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test7.jpeg"),""),
//            StaggeredItemData("강아지8", Uri.parse("android.resource://com.example.testapplication/drawable-v24/stagg_test8.jpeg"),""),
//            )

        val adapter=StaggeredAdapter(list){
            Toast.makeText(requireContext(),"${it.title} clicked",Toast.LENGTH_SHORT).show()
        }
        binding.fragmentScrollRecyclerview.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.fragmentScrollRecyclerview.scroll
        binding.fragmentScrollRecyclerview.adapter=adapter


        binding.fragmentScrollAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{appBarLayout, verticalOffset ->
            if(abs(verticalOffset)-appBarLayout.totalScrollRange==0){
                //접혔을 때
                binding.fragmentScrollTitle.visibility=View.VISIBLE
            }else{
                binding.fragmentScrollTitle.visibility=View.GONE
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
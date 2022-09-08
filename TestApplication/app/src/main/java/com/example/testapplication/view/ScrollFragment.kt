package com.example.testapplication.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.testapplication.StaggeredAdapter
import com.example.testapplication.custom.StickyHeaderItemDecoration
import com.example.testapplication.data.StaggeredItemData
import com.example.testapplication.data.StaggeredViewType
import com.example.testapplication.databinding.FragmentScrollBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class ScrollFragment:Fragment() {


    private var _binding:FragmentScrollBinding?=null
    private val binding:FragmentScrollBinding
        get()=_binding!!

    private lateinit var adapter: StaggeredAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentScrollBinding.inflate(layoutInflater,container,false)



        val uriArray=Array(16){
            val index=(it%8)+1
            val uri=Uri.parse("android.resource://com.example.testapplication/drawable/stagg_test${index}")
            uri
        }
        val list=List(16) {
            when (it) {

                0 -> StaggeredItemData(
                    "강아지${it + 1}",
                    uriArray[it],
                    "",
                    StaggeredViewType.TYPE_SCROLL_H
                )


                1 -> StaggeredItemData(
                    "강아지${it + 1}",
                    uriArray[it],
                    "",
                    StaggeredViewType.TYPE_HEADER
                )


                else -> StaggeredItemData(
                    "강아지${it + 1}",
                    uriArray[it],
                    "",
                    StaggeredViewType.TYPE_ITEM
                )

            }
        }



        adapter=StaggeredAdapter(list){
            Toast.makeText(requireContext(),"${it.title} clicked",Toast.LENGTH_SHORT).show()
        }

        binding.fragmentScrollRecyclerview.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.fragmentScrollRecyclerview.adapter=adapter
        binding.fragmentScrollRecyclerview.addItemDecoration(StickyHeaderItemDecoration(getSectionCallback()))
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

    private fun getSectionCallback(): StickyHeaderItemDecoration.SectionCallback {
        return object : StickyHeaderItemDecoration.SectionCallback {
            override fun isSection(position: Int): Boolean {
                return adapter.isHeader(position)
            }

            override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
                return adapter.getHeaderView(list, position)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }



}
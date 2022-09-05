package com.example.testapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
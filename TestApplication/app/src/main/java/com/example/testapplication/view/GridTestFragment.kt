package com.example.testapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapplication.databinding.FragmentGridTestBinding


class GridTestFragment:Fragment() {
    private var _binding: FragmentGridTestBinding?=null
    private val binding: FragmentGridTestBinding
        get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentGridTestBinding.inflate(layoutInflater,container,false)
        binding.fragmentGridTestGrid.setEditText(binding.fragmentGridTestEdittext)






        return binding.root
    }
}
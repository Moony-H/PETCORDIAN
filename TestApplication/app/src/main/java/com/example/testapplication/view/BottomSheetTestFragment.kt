package com.example.testapplication.view

import ListBottomSheetDialogFragmentFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testapplication.ListBottomSheetDialog
import com.example.testapplication.R
import com.example.testapplication.data.ListBottomSheetData
import com.example.testapplication.data.MutablePair
import com.example.testapplication.databinding.FragmentBottomSheetTestBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetTestFragment: Fragment() {


    private var _binding: FragmentBottomSheetTestBinding? = null
    private val binding: FragmentBottomSheetTestBinding
        get() = _binding!!

    private lateinit var listBottomSheetDialog:ListBottomSheetDialog

    private val list= listOf("아","야","어","여","오","요","우","유","으","이","윀","읔","킼","?")
    private val dataList=List<MutablePair<String,Boolean>>(10) { i -> MutablePair(list[i], false) }
    private var selectedItem:Int=-1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("testing","onCreate")
        _binding = FragmentBottomSheetTestBinding.inflate(layoutInflater, container, false)

        listBottomSheetDialog=
            ListBottomSheetDialog(requireContext(),ListBottomSheetData("그냥 제목",dataList)){text,index,_->
                Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show()
                if(selectedItem!=-1)
                    dataList[selectedItem].second=false
                dataList[index].second=true

                listBottomSheetDialog.notifyItemChanged()
                listBottomSheetDialog.dismiss()

                selectedItem=index


            }

        //listBottomSheetDialog.setPeekHeight(800)
        binding.fragmentBottomSheetTestButton.setOnClickListener{

            listBottomSheetDialog.show()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
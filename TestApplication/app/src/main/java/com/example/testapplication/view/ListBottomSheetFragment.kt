package com.example.testapplication.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.SourceItemListBottomSheetBinding
import com.example.testapplication.databinding.SourceListBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ListBottomSheetFragment(

    private val title:String,
    private val list:List<String>,
    private val onItemClicked: (String, Int, View) -> Unit

):BottomSheetDialogFragment() {


    private var _binding: SourceListBottomSheetBinding? = null
    private val binding: SourceListBottomSheetBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SourceListBottomSheetBinding.inflate(layoutInflater, container, false)
        binding.sourceListBottomSheetTitle.text = title
        binding.sourceListBottomSheetRecycler.adapter = ListBottomSheetAdapter(list, onItemClicked)
        binding.sourceListBottomSheetRecycler.layoutManager=LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog=super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            Log.d("testing","show listener")
            val bottomSheet=it as BottomSheetDialog
            bottomSheet.behavior.isDraggable=false
        }
        return dialog
    }


    inner class ListBottomSheetAdapter(
        private val list: List<String>,
        private val onItemClicked: (String, Int, View) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val binding = SourceItemListBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.binding.root.setOnClickListener {
                onItemClicked(list[position], position, it)
                this@ListBottomSheetFragment.dismiss()
            }
            holder.textView.text = list[position]
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    inner class ViewHolder(val binding: SourceItemListBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView = binding.sourceItemListBottomSheetText

    }
}
package com.example.testapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.data.ListBottomSheetData
import com.example.testapplication.data.MutablePair
import com.example.testapplication.databinding.SourceItemListBottomSheetBinding
import com.example.testapplication.databinding.SourceListBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListBottomSheetDialog(private val context: Context,private val data: ListBottomSheetData,private val onItemClicked:(String,Int,ViewHolder)->Unit) {

    private var _binding: SourceListBottomSheetBinding? = SourceListBottomSheetBinding.inflate(
        LayoutInflater.from(context),
    )

    private val adapter=ListBottomSheetAdapter(data.stringList,onItemClicked)

    private val bottomSheetDialog = BottomSheetDialog(context)


    init {
        _binding?.let {

            it.sourceListBottomSheetTitle.text = data.title

            it.sourceListBottomSheetRecycler.adapter = adapter

            it.sourceListBottomSheetRecycler.layoutManager = LinearLayoutManager(context)

            bottomSheetDialog.behavior.isDraggable = false
            bottomSheetDialog.setContentView(it.root)


        }

    }


    inner class ListBottomSheetAdapter(
        private val list: List<MutablePair<String,Boolean>>,
        private val onItemClicked: (String, Int, ViewHolder) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d("on","create")
            val binding = SourceItemListBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.root.setOnClickListener {
                onItemClicked(list[position].first, position, holder)
            }

            holder.textView.text = list[position].first
            if(list[position].second){
                holder.imageView.setImageResource(R.color.black)
            }else{
                holder.imageView.setImageResource(R.color.white)
            }

        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    inner class ViewHolder(val binding: SourceItemListBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textView = binding.sourceItemListBottomSheetText
        val imageView = binding.sourceItemListBottomSheetImage
    }

    fun show() {
        bottomSheetDialog.show()
    }

    fun dismiss() {
        bottomSheetDialog.dismiss()
    }

    fun freeBinding() {
        _binding = null
    }

    fun setState(state: Int) {
        bottomSheetDialog.behavior.state = state
    }

    fun setPeekHeight(height: Int) {
        bottomSheetDialog.behavior.peekHeight = height
    }

    fun notifyItemChanged(){

        adapter.notifyDataSetChanged()
    }


}
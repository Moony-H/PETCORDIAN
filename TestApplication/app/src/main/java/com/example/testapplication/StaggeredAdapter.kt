package com.example.testapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.data.StaggeredItemData
import com.example.testapplication.databinding.SourceItemStaggeredBinding

class StaggeredAdapter(private val dataList:List<StaggeredItemData>,private val onItemClicked:(data:StaggeredItemData)->Unit):RecyclerView.Adapter<StaggeredAdapter.StaggeredViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggeredViewHolder {

        return StaggeredViewHolder(SourceItemStaggeredBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: StaggeredViewHolder, position: Int) {

        holder.bind(dataList[position],onItemClicked)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class StaggeredViewHolder(private val binding: SourceItemStaggeredBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val image = binding.sourceItemStaggeredImage
        private val text=binding.sourceItemStaggeredText
        private lateinit var data: StaggeredItemData


        fun bind(data: StaggeredItemData,onClicked: (data:StaggeredItemData) -> Unit) {

            this.data=data
            image.setImageURI(data.image)
            text.text=data.title

            binding.root.setOnClickListener {
                onClicked(data)
            }
        }

    }
}
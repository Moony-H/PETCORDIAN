package com.example.testapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.testapplication.data.StaggeredItemData
import com.example.testapplication.data.StaggeredViewType
import com.example.testapplication.databinding.SourceItemStaggeredBinding
import com.example.testapplication.databinding.SourceItemStaggeredHeaderBinding
import com.example.testapplication.databinding.SourceItemStaggeredScrollBinding
import java.lang.IllegalStateException

class StaggeredAdapter(private val dataList:List<StaggeredItemData>,private val onItemClicked:(data:StaggeredItemData)->Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            StaggeredViewType.TYPE_SCROLL_H.TYPE->{
                HorizontalScrollViewHolder(SourceItemStaggeredScrollBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            StaggeredViewType.TYPE_HEADER.TYPE->{
                HeaderViewHolder(SourceItemStaggeredHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            StaggeredViewType.TYPE_ITEM.TYPE->{
                ItemViewHolder(SourceItemStaggeredBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else-> throw IllegalStateException("cannot found view holder type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(dataList[position].viewType){

            StaggeredViewType.TYPE_SCROLL_H->{
                val lp=holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                lp.isFullSpan=true
            }

            StaggeredViewType.TYPE_HEADER->{
                val lp=holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                lp.isFullSpan=true
            }

            StaggeredViewType.TYPE_ITEM->{
                (holder as ItemViewHolder).bind(dataList[position],onItemClicked)
            }
        }



    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType.TYPE
    }

    fun isHeader(position: Int) = dataList[position].viewType==StaggeredViewType.TYPE_HEADER || dataList[position].viewType==StaggeredViewType.TYPE_SCROLL_H

    fun getHeaderView(list: RecyclerView, position: Int): View? {
        val item = dataList[position]

        val binding = SourceItemStaggeredHeaderBinding.inflate(LayoutInflater.from(list.context), list, false)
        return binding.root
    }

    inner class ItemViewHolder(private val binding: SourceItemStaggeredBinding) :
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

    inner class HeaderViewHolder(private val binding:SourceItemStaggeredHeaderBinding):RecyclerView.ViewHolder(binding.root)

    inner class HorizontalScrollViewHolder(private val binding:SourceItemStaggeredScrollBinding):RecyclerView.ViewHolder(binding.root)
}
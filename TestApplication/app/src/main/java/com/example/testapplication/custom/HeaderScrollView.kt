package com.example.testapplication.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.example.testapplication.data.MutablePair
import java.lang.IllegalStateException

class HeaderScrollView:NestedScrollView {

    private var headers=ArrayList<View>()
    private lateinit var viewGroup:ViewGroup
    private lateinit var headersHeadBounds:Array<MutablePair<Int,Int>>
    private var nowHeaderIndex=0
    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context,attrs:AttributeSet):super(context, attrs){
        init()
    }

    private fun init(){


        this.viewTreeObserver.addOnDrawListener {
            if(headersHeadBounds[nowHeaderIndex].first<=scrollY && headersHeadBounds[nowHeaderIndex].second>=scrollY) {
                stickHeader()
            }else{

                releaseHeader()


            }

        }

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(childCount<=0)
            throw IllegalStateException("scrollview does not have child")
        val child=this.getChildAt(0)
        if(child is ViewGroup)
            viewGroup=child
        initHeadersHeadBounds()
    }

    private fun stickHeader(){
        headers[nowHeaderIndex].translationY = (scrollY - headersHeadBounds[nowHeaderIndex].first).toFloat()

    }
    private fun releaseHeader(){


        if(headersHeadBounds[nowHeaderIndex].first>scrollY){
            headers[nowHeaderIndex].translationY=0f
            if(nowHeaderIndex!=0)
                nowHeaderIndex-=1
        }
        else if(headersHeadBounds[nowHeaderIndex].second<scrollY){
            headers[nowHeaderIndex].translationY= (headersHeadBounds[nowHeaderIndex].second-headersHeadBounds[nowHeaderIndex].first).toFloat()
            nowHeaderIndex+=1

        }

        if(nowHeaderIndex<0)
            nowHeaderIndex=0
        headersHeadBounds[nowHeaderIndex]
    }



    fun setHeaders(viewList: ArrayList<View>){
        headers=viewList
        headersHeadBounds=Array(headers.size) { MutablePair(0,0) }

    }




    private fun initHeadersHeadBounds(){
        headers.forEachIndexed { index,view->
            view.translationZ=1f
            if(index==headers.size-1){
                headersHeadBounds[index].first=headers[index].top
                headersHeadBounds[index].second=viewGroup.height
                return@forEachIndexed
            }

            headersHeadBounds[index].first=headers[index].top
            headersHeadBounds[index].second=headers[index+1].top-headers[index].height
        }
    }

    fun getNowHeader():View{
        return headers[nowHeaderIndex]
    }
}
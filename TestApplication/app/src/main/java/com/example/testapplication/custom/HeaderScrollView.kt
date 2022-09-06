package com.example.testapplication.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.example.testapplication.data.MutablePair
import java.lang.IllegalStateException

class HeaderScrollView:NestedScrollView {

    private var headers=ArrayList<View>()
    private lateinit var viewGroup:ViewGroup
    private lateinit var headersHeadBounds:Array<MutablePair<Int,Int>>
    private var nowHeader=0
    private var stickHeader:View?=null
    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context,attrs:AttributeSet):super(context, attrs){
        init()
    }

    private fun init(){


        this.viewTreeObserver.addOnDrawListener {
            if(headersHeadBounds[nowHeader].first<=scrollY && headersHeadBounds[nowHeader].second>=scrollY) {
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
        headers[nowHeader].translationY = (scrollY - headersHeadBounds[nowHeader].first).toFloat()

    }
    private fun releaseHeader(){


        if(headersHeadBounds[nowHeader].first>scrollY){
            headers[nowHeader].translationY=0f
            if(nowHeader!=0)
                nowHeader-=1
        }
        else if(headersHeadBounds[nowHeader].second<scrollY)
            nowHeader+=1
        if(nowHeader<0)
            nowHeader=0
        headersHeadBounds[nowHeader]
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
}
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

    private var nowHeaderIndex=0
    private var headerStuckOnce=true

    private var onHeaderStuck={i:Int,view:View->}
    private var onScrolling={_:Int->}

    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context,attrs:AttributeSet):super(context, attrs){
        init()
    }

    private fun init(){



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

    private fun stickHeader(y:Int) {
        headers[nowHeaderIndex].translationY =
            (y - headersHeadBounds[nowHeaderIndex].first).toFloat()
        if (headerStuckOnce) {
            onHeaderStuck(nowHeaderIndex, headers[nowHeaderIndex])
            headerStuckOnce = false
        }
    }
    private fun releaseHeader(y:Int){

        headerStuckOnce=true
        if(headersHeadBounds[nowHeaderIndex].first>y){
            headers[nowHeaderIndex].translationY=0f
            if(nowHeaderIndex!=0)
                nowHeaderIndex-=1
        }
        else if(headersHeadBounds[nowHeaderIndex].second<y){
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

    override fun onScrollChanged(l: Int, y: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, y, oldl, oldt)
        Log.d("testing","y is $y")
        if(headersHeadBounds[nowHeaderIndex].first<=y && headersHeadBounds[nowHeaderIndex].second>=y) {
            stickHeader(y)
        }else{

            releaseHeader(y)


        }

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
    fun setOnHeaderStuck(onHeaderStuck:(index:Int,header:View)->Unit){
        this.onHeaderStuck=onHeaderStuck
    }

    fun setOnScrolling(onScrolling:(y:Int)->Unit){
        this.onScrolling=onScrolling
    }
}
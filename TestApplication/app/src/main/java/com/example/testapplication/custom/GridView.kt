package com.example.testapplication.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class GridView:AppCompatEditText {

    private var columns=7
    private var row=7

    private var lineWidth=5f
    constructor(context: Context):super(context){
        //init()
    }
    constructor(context: Context,attrs:AttributeSet):super(context, attrs){
        //init()
    }

    private fun init(){
        val fontScale=context.resources.displayMetrics.scaledDensity
        this.setPadding(0,0,0,0)
        this.letterSpacing=1/fontScale

    }

//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//
//        this.textSize
//        canvas?.let{
//        }
//    }
}
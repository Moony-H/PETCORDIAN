package com.example.testapplication.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testapplication.databinding.SourceGridTextViewCellBinding

class GridTextItemView:ConstraintLayout {
    private lateinit var editText: EditText
    constructor(context: Context):super(context){
        init()
    }
    constructor(context: Context,attrs:AttributeSet):super(context, attrs){
        init()
    }

    private fun init(){
        editText=EditText(context)


    }

    fun setLineWidth(dp:Int){

    }

    private fun convertDpToPixel(dp:Int):Int{
        return (dp*context.resources.displayMetrics.density).toInt()
    }

}
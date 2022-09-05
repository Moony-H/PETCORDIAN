package com.example.testapplication.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testapplication.databinding.SourceCalendarHeaderBinding

class CalendarHeaderView:ConstraintLayout {
    private lateinit var binding:SourceCalendarHeaderBinding
    private lateinit var prevButton:Button
    private lateinit var nextButton:Button
    private lateinit var monthTextView:TextView
    private lateinit var yearTextView:TextView
    constructor(context:Context):super(context){
        init()
    }
    constructor(context: Context,attrs:AttributeSet):super(context, attrs){
        init()
    }

    private fun init(){
        binding= SourceCalendarHeaderBinding.inflate(LayoutInflater.from(context),this)
        prevButton=binding.sourceCalendarViewLeftButton
        nextButton=binding.sourceCalendarViewRightButton
        monthTextView=binding.sourceCalendarViewMonth
        yearTextView=binding.sourceCalendarViewYear

    }

    fun setMonthText(month:String){
        monthTextView.text=month
    }

    fun setYearText(year:String){
        yearTextView.text=year
    }

    fun setPrevButtonClicked(onClickListener: OnClickListener){
        prevButton.setOnClickListener(onClickListener)
    }

    fun setNextButtonClicked(onClickListener: OnClickListener){
        nextButton.setOnClickListener(onClickListener)
    }
}
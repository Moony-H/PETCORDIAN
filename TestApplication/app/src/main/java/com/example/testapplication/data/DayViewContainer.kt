package com.example.testapplication.data

import android.view.View
import androidx.core.content.ContextCompat
import com.example.testapplication.R
import com.example.testapplication.databinding.SourceDayViewContainerBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view:View):ViewContainer(view) {
    val textView=SourceDayViewContainerBinding.bind(view).sourceDayViewContainerText
    private var selected=false
    init {
        view.setOnClickListener {
            if(!selected){
                view.background=ContextCompat.getDrawable(view.context, R.drawable.test_black)
                selected=true
            }

            else{
                view.background=null
                selected=false
            }

        }
    }
}
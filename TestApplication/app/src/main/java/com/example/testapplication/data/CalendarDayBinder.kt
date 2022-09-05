package com.example.testapplication.data

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder

class CalendarDayBinder:DayBinder<DayViewContainer> {
    override fun create(view: View): DayViewContainer = DayViewContainer(view)

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        if(day.owner==DayOwner.THIS_MONTH){
            container.textView.text = day.date.dayOfMonth.toString()
        }else{
            container.textView.text=""
        }

    }
}
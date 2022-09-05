package com.example.testapplication.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testapplication.R
import com.example.testapplication.data.CalendarDayBinder
import com.example.testapplication.databinding.FragmentMainBinding
import com.example.testapplication.viewModel.MainViewModel
import java.time.temporal.WeekFields
import java.util.*

class MainFragment:Fragment(),View.OnClickListener {

    private var _binding:FragmentMainBinding?=null
    private val binding:FragmentMainBinding
            get()=_binding!!


    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        FragmentMainBinding.inflate(layoutInflater,container,false)
        val calendarView=binding.fragmentMainCalendar

        calendarView.dayBinder= CalendarDayBinder()



        val firstMonth = mainViewModel.currentYearMonth.value!!.minusMonths(10)
        val lastMonth = mainViewModel.currentYearMonth.value!!.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek



        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(mainViewModel.currentYearMonth.value!!)
        binding.fragmentMainCalendarHeader.setNextButtonClicked(this)
        binding.fragmentMainCalendarHeader.setPrevButtonClicked(this)

        mainViewModel.currentYearMonth.observe(viewLifecycleOwner){
            binding.fragmentMainCalendarHeader.setMonthText(it.month.value.toString())
            binding.fragmentMainCalendarHeader.setYearText(it.year.toString())
            Log.d("test","month: ${it.monthValue}")
            binding.fragmentMainCalendar.smoothScrollToMonth(it)
        }




        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onClick(view: View) {
        Log.d("MainFragment","button clicked")
        when(view.id){
            R.id.source_calendar_view_left_button ->{
                mainViewModel.addMonthValue(-1)
                //binding.fragmentMainCalendar.notifyMonthChanged(currentMonth)
                Log.d("MainFragment","left button clicked")
            }
            R.id.source_calendar_view_right_button ->{
                mainViewModel.addMonthValue(1)
                //binding.fragmentMainCalendar.notifyMonthChanged(currentMonth)

                Log.d("MainFragment","right button clicked")
            }
        }
    }

}
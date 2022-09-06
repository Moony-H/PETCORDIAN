package com.example.testapplication.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.YearMonth

class MainViewModel:ViewModel() {
    private var _currentYearMonth=MutableLiveData<YearMonth>(YearMonth.now())
    val currentYearMonth:LiveData<YearMonth>
        get()=_currentYearMonth



    fun addMonthValue(num:Long){
        if(num<0){
            Log.d("test","num is minus")
            _currentYearMonth.value=_currentYearMonth.value?.minusMonths(-num)
        }

        else{
            Log.d("test","num is plus")
            _currentYearMonth.value=_currentYearMonth.value?.plusMonths(num)
        }

    }

}
package com.example.testapplication

import android.app.Application
import android.content.Context

class TestApplication:Application() {
    init{
        instance=this
    }

    companion object{
        lateinit var instance:TestApplication
        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
}
package com.example.testapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.testapplication.R
import com.example.testapplication.databinding.ActivityMainBinding

class MainActivity:AppCompatActivity() {


    //할일
    //coordinnate 써서 달짜의 일기 텍스트가 화면 앱바의 가운데로 애니메이션 하여 들어가기.

    //sticky header
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        supportFragmentManager.commit {
            add(R.id.activity_main_container, ScrollFragment())
        }

        setContentView(binding.root)
    }
}
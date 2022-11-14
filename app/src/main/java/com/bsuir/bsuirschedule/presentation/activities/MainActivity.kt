package com.bsuir.bsuirschedule.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bsuir.bsuirschedule.databinding.ActivityMainBinding
import com.bsuir.bsuirschedule.service.ScheduleService
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scheduleAlarm = ScheduleService(this)
        scheduleAlarm.setExactAlarm(Date().time + 3000)
    }

}



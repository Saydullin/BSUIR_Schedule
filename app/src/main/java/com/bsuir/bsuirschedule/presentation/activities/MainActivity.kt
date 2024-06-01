package com.bsuir.bsuirschedule.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsuir.bsuirschedule.data.logger.Logger
import com.bsuir.bsuirschedule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        Logger(this).log("Hello, Saydullin!")

        setContentView(binding.root)
    }

}



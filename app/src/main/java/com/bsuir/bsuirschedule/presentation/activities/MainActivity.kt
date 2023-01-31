package com.bsuir.bsuirschedule.presentation.activities

import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bsuir.bsuirschedule.data.repository.SharedPrefsRepositoryImpl
import com.bsuir.bsuirschedule.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

}



package com.bsuir.bsuirschedule.presentation.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class BaseActivity : AppCompatActivity() {

    companion object {
        var dLocale: Locale? = null
    }

    init {
        updateConfig()
    }

    private fun updateConfig() {
        if (dLocale == null) return

        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        applyOverrideConfiguration(configuration)
    }

}
package com.bsuir.bsuirschedule.presentation.activities

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ActivityWidgetAddBinding

class WidgetAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWidgetAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.lightThemeWidgetPlaceholder.animate().alpha(0f).duration = 300
                binding.darkThemeWidgetPlaceholder.animate().alpha(1f).duration = 300
            } else {
                binding.darkThemeWidgetPlaceholder.animate().alpha(0f).duration = 300
                binding.lightThemeWidgetPlaceholder.animate().alpha(1f).duration = 300
            }
        }

        binding.createWidget.setOnClickListener {
            val appWidgetId = intent?.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val remoteViews = RemoteViews(this.packageName, R.layout.today_schedule_widget)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }

}



package com.bsuir.bsuirschedule.presentation.activities

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ActivityWidgetAddBinding
import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.WidgetManagerUseCase
import com.bsuir.bsuirschedule.presentation.widgets.ScheduleWidget
import com.bsuir.bsuirschedule.receiver.WidgetAddReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WidgetAddActivity : AppCompatActivity(), KoinComponent {

    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()
    private val widgetManagerUseCase: WidgetManagerUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWidgetAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val unknownScheduleTitle = getString(R.string.unknown)
        val defaultScheduleTitle = sharedPrefsUseCase.getDefaultScheduleTitle() ?: unknownScheduleTitle
        val scheduleTitle = intent.extras?.getString(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_TITLE) ?: defaultScheduleTitle

        binding.scheduleTitle.text = scheduleTitle

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
            val isDarkTheme = binding.darkThemeSwitch.isChecked
            val isHaveToCreateWidget = intent?.extras?.getBoolean(
                WidgetSettings.EXTRA_HAVE_TO_ADD_APPWIDGET,
                false
            ) ?: false
            val scheduleId = intent?.extras?.getInt(
                WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_ID,
                -1
            ) ?: -1

            if (isHaveToCreateWidget) {
                createNewWidget(isDarkTheme, scheduleId)
            } else {
                addConfigureWidget(isDarkTheme, scheduleId)
            }

            finish()
        }
    }

    private fun addConfigureWidget(isDarkTheme: Boolean, scheduleId: Int) {
        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val widgetSettings = WidgetSettings(
            appWidgetId,
            scheduleId,
            isDarkTheme
        )
        runBlocking(Dispatchers.IO) {
            widgetManagerUseCase.saveWidgetSettings(widgetSettings)
        }
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val remoteViews = RemoteViews(this.packageName, R.layout.today_schedule_widget)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        val widgetIntent = Intent(this, ScheduleWidget::class.java)
        widgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, ScheduleWidget::class.java))
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(widgetIntent)
    }

    private fun createNewWidget(isDarkTheme: Boolean, scheduleId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mAppWidgetManager: AppWidgetManager =
                ContextCompat.getSystemService(this, AppWidgetManager::class.java) ?: return
            val myProvider = ComponentName(this, ScheduleWidget::class.java)

            if (mAppWidgetManager.isRequestPinAppWidgetSupported) {
                val pinnedWidgetCallbackIntent =
                    Intent(this, WidgetAddReceiver::class.java)
                pinnedWidgetCallbackIntent.putExtra(WidgetSettings.EXTRA_APPWIDGET_IS_DARK_THEME, isDarkTheme)
                pinnedWidgetCallbackIntent.putExtra(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_ID, scheduleId)
                pinnedWidgetCallbackIntent.action = "unique" + System.currentTimeMillis()
                val successCallback = PendingIntent.getBroadcast(
                    this, 0,
                    pinnedWidgetCallbackIntent, PendingIntent.FLAG_MUTABLE
                )
                mAppWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
            } else {
                Toast.makeText(this, "Not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Only 8 Android+", Toast.LENGTH_SHORT).show()
        }
    }

}



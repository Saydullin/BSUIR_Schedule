package com.bsuir.bsuirschedule.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.TimePickerDialogBinding
import com.bsuir.bsuirschedule.domain.models.Time

class TimePickerDialog(
    ctx: Context,
    private val onTimeSelected: (Time) -> Unit
): Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = TimePickerDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        binding.timePicker.setIs24HourView(true)

        binding.doneButton.setOnClickListener {
            val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.timePicker.hour
            } else {
                binding.timePicker.currentHour
            }
            val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.timePicker.minute
            } else {
                binding.timePicker.currentMinute
            }
            val selectedTime = Time(
                hour = hour,
                minute = minute
            )
            onTimeSelected(selectedTime)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

    }

}
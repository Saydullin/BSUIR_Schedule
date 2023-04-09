package com.bsuir.bsuirschedule.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.AlertDialogBinding

class AlertDialog(
    ctx: Context,
    private val title: String,
    private val onAgree: () -> Unit,
) : Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AlertDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        binding.alertTitle.text = title

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.agreeButton.setOnClickListener {
            onAgree()
            dismiss()
        }
    }

}
package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.InfoDialogBinding

class InfoDialog(
    private val title: String,
    private val description: String
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = InfoDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        isCancelable = true

        binding.title.text = title
        binding.description.text = description

        binding.doneButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}
package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.PermissionDialogBinding

class PermissionDialog(
    private val title: String,
    private val description: String,
    private val permissionGranted: () -> Unit,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PermissionDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).duration = 300

        isCancelable = true

        binding.title.text = title
        binding.description.text = description

        binding.doneButton.setOnClickListener {
            permissionGranted()
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}
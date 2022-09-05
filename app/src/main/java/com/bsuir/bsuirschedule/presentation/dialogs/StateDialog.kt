package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.StateDialogBinding
import com.bsuir.bsuirschedule.domain.models.StateStatus
import com.bsuir.bsuirschedule.presentation.utils.ErrorMessage

class StateDialog(private val stateStatus: StateStatus): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = StateDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

        when(stateStatus.state) {
            StateStatus.SUCCESS_STATE -> {
                binding.icon.setImageResource(R.drawable.ic_success_icon)
                binding.title.text = resources.getString(R.string.success_dialog_title)
                binding.caption.text = resources.getString(R.string.success_dialog_caption)
            }
            StateStatus.INFO_STATE -> {
                binding.icon.setImageResource(R.drawable.ic_info_icon)
                binding.title.text = resources.getString(R.string.info_dialog_title)
                binding.caption.text = stateStatus.message
            }
            StateStatus.ERROR_STATE -> {
                val errorMessage = ErrorMessage(context!!).get(stateStatus.type)

                binding.icon.setImageResource(R.drawable.ic_error_icon)
                binding.title.text = errorMessage.title
                binding.caption.text = errorMessage.caption
            }
        }

        return binding.root
    }

}

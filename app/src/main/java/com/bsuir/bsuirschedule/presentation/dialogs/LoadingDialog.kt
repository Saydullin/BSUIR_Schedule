package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.LoadingDialogBinding
import com.bsuir.bsuirschedule.domain.models.LoadingStatus

class LoadingDialog(private val loadingStatus: LoadingStatus): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoadingDialogBinding.inflate(inflater)

        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

        when(loadingStatus.code) {
            LoadingStatus.LOAD_SCHEDULE -> {
                binding.title.text = resources.getString(R.string.load_schedule)
                binding.caption.text = resources.getString(R.string.load_schedule_caption)
            }
            else -> {
                binding.title.text = resources.getString(R.string.load)
                binding.caption.text = resources.getString(R.string.load_caption)
            }
        }

        binding.progressBar.progress

        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(200).start()

        return binding.root
    }

}



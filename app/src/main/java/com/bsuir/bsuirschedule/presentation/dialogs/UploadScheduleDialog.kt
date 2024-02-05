package com.bsuir.bsuirschedule.presentation.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.UploadScheduleDialogBinding
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class UploadScheduleDialog(
    private val savedSchedule: SavedSchedule,
    private val employeeSubject: EmployeeSubject?,
    private val onUploadSubmit: (savedSchedule: SavedSchedule) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = UploadScheduleDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        if (employeeSubject != null && !employeeSubject.email.isNullOrEmpty()) {
            val copiedText = getString(R.string.text_copied)
            binding.letterContainer.visibility = View.VISIBLE
            binding.copyLetterButton.text = employeeSubject.email
            binding.copyLetterButton.setOnClickListener {
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(
                    "com.bsuir.bsuirschedule",
                    binding.copyLetterButton.text
                )
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, copiedText, Toast.LENGTH_SHORT).show()
            }
            binding.emailButton.setOnClickListener {
                val recipient = employeeSubject.email
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.hello_name,
                        "${employeeSubject.firstName} ${employeeSubject.middleName}")
                )
                emailIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(recipient)
                )

                try {
                    startActivity(emailIntent)
                } catch(e: Exception) {
                    val errorOpenGmailText = getString(R.string.error_open_gmail)
                    Toast.makeText(context, errorOpenGmailText, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.letterContainer.visibility = View.GONE
        }

        binding.scheduleHeader.setSavedSchedule(savedSchedule)

        if (!savedSchedule.isGroup) {
            binding.scheduleHeader.setImageClickListener {
                val imageViewDialog = savedSchedule.employee.photoLink?.let {
                    ImageViewDialog(
                        requireContext(),
                        it,
                        savedSchedule.employee.getFullName(),
                    )
                }
                imageViewDialog?.show()
            }
        }

        binding.agreeButton.setOnClickListener {
            onUploadSubmit(savedSchedule)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}
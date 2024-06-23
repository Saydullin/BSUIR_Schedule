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
import com.bsuir.bsuirschedule.databinding.ActiveScheduleDialogBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager

class ScheduleDialog(
    private val schedule: Schedule,
    private val update: (savedSchedule: SavedSchedule) -> Unit,
    private val delete: (savedSchedule: SavedSchedule) -> Unit
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActiveScheduleDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        val lastUpdateText = resources.getString(R.string.last_update, schedule.getLastUpdateText())
        val courseText = resources.getString(R.string.course)
        val moreText = resources.getString(R.string.more)
        val selectedSubgroup = schedule.settings.subgroup.selectedNum
        binding.lastUpdate.text = lastUpdateText
        binding.scheduleSubgroup.text = if (selectedSubgroup == 0) {
            resources.getString(R.string.selected_all_subgroups)
        } else {
            resources.getString(R.string.selected_subgroup, selectedSubgroup)
        }

        val examsDatePeriod = if (!schedule.isExamsNotExist()) {
            resources.getString(
                R.string.exams_date_period,
                schedule.getDateText(schedule.startExamsDate),
                schedule.getDateText(schedule.endExamsDate)
            )
        } else {
            resources.getString(R.string.exams_empty_date_period)
        }
        binding.examsDate.text = examsDatePeriod

        val scheduleDatePeriod = if (schedule.startDate.isNotEmpty() && schedule.endDate.isNotEmpty()) {
            if (schedule.previousSchedules.isNotEmpty()) {
                resources.getString(
                    R.string.previous_schedule_date_period,
                    schedule.getDateText(schedule.startDate),
                    schedule.getDateText(schedule.endDate)
                )
            } else {
                resources.getString(
                    R.string.schedule_date_period,
                    schedule.getDateText(schedule.startDate),
                    schedule.getDateText(schedule.endDate)
                )
            }
        } else {
            resources.getString(R.string.schedule_empty_date_period)
        }

        binding.scheduleDate.text = scheduleDatePeriod

        if (!schedule.employee.email.isNullOrEmpty()) {
            val copiedText = getString(R.string.text_copied)
            binding.letterContainer.visibility = View.VISIBLE
            binding.copyLetterButton.text = schedule.employee.email
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
                val recipient = schedule.employee.email
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                emailIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.hello_name,
                        "${schedule.employee.firstName} ${schedule.employee.middleName}")
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

        with(binding.scheduleHeaderView) {
            if (schedule.isGroup()) {
                val group = schedule.group
                setTitle(group.name ?: "")
                setImage(R.drawable.ic_group_placeholder)
                setDescription(group.getFacultyAndSpecialityAbbr())
                setSecondTitle("${group.course} $courseText")
                setSecondSubTitle(group.getFacultyAndSpecialityAbbr())
                binding.scheduleSubtitles.text = group.getFacultyAndSpecialityFull()
            } else {
                val employee = schedule.employee
                setTitle(employee.getFullName())
                setImage(employee.photoLink ?: "")
                setDescription(employee.getRankAndDegree())
                setSecondSubTitle(employee.getShortDepartments(moreText))
                if (employee.departmentsList.isNullOrEmpty()) {
                    val noDepartments = resources.getString(R.string.active_schedule_no_departments)
                    binding.scheduleSubtitles.text = noDepartments
                } else {
                    binding.scheduleSubtitles.text = employee.getFullDepartments("\n\n")
                }

                setImageClickListener {
                    val imageViewDialog = ImageViewDialog(
                        requireContext(),
                        employee.photoLink ?: "",
                        employee.getFullName()
                    )
                    imageViewDialog.show()
                    dismiss()
                }
            }
        }

        if (schedule.subjectNow != null) {
            val subjectManager = SubjectManager(schedule.subjectNow!!, context!!)
            binding.scheduleLocationNow.text = subjectManager.getSubjectDate()
        } else {
            val subjectNowText = resources.getString(R.string.no_subject_now)
            binding.scheduleLocationNow.text = subjectNowText
        }

        binding.deleteButton.setOnClickListener {
            delete(schedule.toSavedSchedule())
            dismiss()
        }

        binding.updateButton.setOnClickListener {
            update(schedule.toSavedSchedule())
            dismiss()
        }

        return binding.root
    }

}



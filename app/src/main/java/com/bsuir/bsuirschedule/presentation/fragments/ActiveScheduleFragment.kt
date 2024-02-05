package com.bsuir.bsuirschedule.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.dialogs.ScheduleDialog
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentActiveScheduleBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.presentation.activities.WidgetAddActivity
import com.bsuir.bsuirschedule.presentation.dialogs.DeleteScheduleDialog
import com.bsuir.bsuirschedule.presentation.dialogs.ImageViewDialog
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager
import com.bsuir.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import com.bsuir.bsuirschedule.presentation.views.ScheduleAction
import org.koin.androidx.navigation.koinNavGraphViewModel
import java.util.*

class ActiveScheduleFragment : Fragment() {

    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val savedScheduleVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentActiveScheduleBinding.inflate(inflater)

        val deleteSchedule = { savedSchedule: SavedSchedule ->
            savedScheduleVM.deleteSchedule(savedSchedule)
            groupScheduleVM.deleteSchedule(savedSchedule)
            if (savedSchedule.isGroup) {
                savedSchedule.group.isSaved = false
                groupItemsVM.saveGroupItem(savedSchedule.group)
            } else {
                savedSchedule.employee.isSaved = false
                employeeItemsVM.saveEmployeeItem(savedSchedule.employee)
            }
        }

        val deleteWarning = { savedSchedule: SavedSchedule ->
            val deleteScheduleDialog = DeleteScheduleDialog(savedSchedule = savedSchedule, agreeCallback = deleteSchedule)
            deleteScheduleDialog.show(parentFragmentManager, "WarningDialog")
        }

        val updateSchedule = { savedSchedule: SavedSchedule ->
            savedSchedule.lastUpdateTime = Date().time
            savedScheduleVM.setActiveSchedule(savedSchedule)
            if (savedSchedule.isGroup) {
                groupScheduleVM.getGroupScheduleAPI(savedSchedule.group, isUpdate = true)
            } else {
                groupScheduleVM.getEmployeeScheduleAPI(savedSchedule.employee, isUpdate = true)
            }
        }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            val selectedSubgroup = schedule.settings.subgroup.selectedNum

            with (binding.scheduleHeaderView) {
                setSubgroupItems(schedule.subgroups)

                if (schedule.isGroup()) {
                    val group = schedule.group
                    setTitle(group.getTitleOrName())
                    setImage(R.drawable.ic_group_placeholder)
                    setDescription(group.getFacultyAndSpecialityAbbr())
                } else {
                    val employee = schedule.employee
                    val moreText = getString(R.string.more)
                    setTitle(employee.getTitleOrFullName())
                    setImage(employee.photoLink)
                    setDescription(employee.getShortDepartments(moreText))
                    setImageClickListener {
                        val imageViewDialog = ImageViewDialog(
                            requireContext(),
                            employee.photoLink,
                            employee.getFullName(),
                        )
                        imageViewDialog.show()
                    }
                }
                setExamsIcon(!schedule.isExamsNotExist())
                setLocationText(getCurrentSubject(schedule.subjectNow))

                if (selectedSubgroup == 0) {
                    setSubgroupText(resources.getString(R.string.all_subgroups_short))
                } else {
                    setSubgroupText(selectedSubgroup.toString())
                }

                setSubgroupListener { subgroupNum ->
                    val scheduleSettings = schedule.settings
                    if (scheduleSettings.subgroup.selectedNum != subgroupNum) {
                        scheduleSettings.subgroup.selectedNum = subgroupNum
                        groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
                    }
                }
            }
            binding.scheduleHeaderView.isExistExams(!schedule.isExamsNotExist())
            binding.scheduleHeaderView.setMenuListener {
                when (it) {
                    ScheduleAction.DIALOG_OPEN -> {
                        val scheduleDialog = ScheduleDialog(
                            schedule = schedule,
                            delete = deleteWarning,
                            update = updateSchedule)
                        scheduleDialog.isCancelable = true
                        scheduleDialog.show(parentFragmentManager, "savedScheduleDialog")
                    }
                    ScheduleAction.SETTINGS -> {
                        Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_scheduleSettingsFragment)
                    }
                    ScheduleAction.UPDATE_HISTORY -> {
                        Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_scheduleUpdateHistoryFragment)
                    }
                    ScheduleAction.UPDATE -> {
                        updateSchedule(schedule.toSavedSchedule())
                    }
                    ScheduleAction.ADD_SUBJECT -> {
                        Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_scheduleSubjectAddFragment)
                    }
                    ScheduleAction.SHARE -> {
                        Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                    }
                    ScheduleAction.WIDGET_ADD -> {
                        val intent = Intent(requireActivity(), WidgetAddActivity::class.java)
                        intent.putExtra(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_TITLE, schedule.getTitle())
                        intent.putExtra(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_ID, schedule.id)
                        intent.putExtra(WidgetSettings.EXTRA_HAVE_TO_ADD_APPWIDGET, true)
                        startActivity(intent)
                    }
                    ScheduleAction.EXAMS -> {
                        Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_scheduleExamsFragment)
                    }
                    ScheduleAction.DELETE -> {
                        deleteWarning(schedule.toSavedSchedule())
                    }
                }
            }

        }

        return binding.root
    }

    private fun getCurrentSubject(subject: ScheduleSubject?): String {
        return if (subject != null) {
            val subjectManager = SubjectManager(subject, context!!)
            subjectManager.getSubjectDate()
        } else {
            resources.getString(R.string.no_subject_now)
        }
    }

}



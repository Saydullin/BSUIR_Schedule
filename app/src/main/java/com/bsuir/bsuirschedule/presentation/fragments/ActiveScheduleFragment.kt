package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.dialogs.SavedScheduleDialog
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentActiveScheduleBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.presentation.dialogs.DeleteScheduleDialog
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
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentActiveScheduleBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            val selectedSubgroup = schedule.settings.subgroup.selectedNum

            with (binding.scheduleHeaderView) {
                setSubgroupItems(schedule.subgroups)

                if (schedule.isGroup()) {
                    val group = schedule.group
                    val courseText = getString(R.string.course)
                    setTitle(group.getTitleOrName())
                    setImageDrawable(R.drawable.ic_group_placeholder)
                    setDescription(group.getFacultyAndSpecialityAbbr())
//                    binding.scheduleCourse.text = "${group.course} $courseText"
                } else {
                    val employee = schedule.employee
                    setTitle(employee.getTitleOrFullName())
                    setImage(employee.photoLink)
                    setDescription(employee.getRankAndDegree())
                    binding.scheduleHeaderView.setDescription(employee.getRankAndDegree())
                }
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
        }

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

        binding.scheduleHeaderView.setMenuListener {
            val activeSchedule = groupScheduleVM.scheduleStatus.value ?: return@setMenuListener
            when (it) {
                ScheduleAction.DIALOG_OPEN -> {
                    val savedScheduleDialog = SavedScheduleDialog(
                        schedule = activeSchedule,
                        delete = deleteWarning,
                        update = updateSchedule)
                    savedScheduleDialog.isCancelable = true
                    savedScheduleDialog.show(parentFragmentManager, "savedScheduleDialog")
                }
                ScheduleAction.SETTINGS -> {
                    Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                }
                ScheduleAction.UPDATE -> {
                    updateSchedule(activeSchedule.toSavedSchedule())
                }
                ScheduleAction.EDIT -> {
                    Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                }
                ScheduleAction.SHARE -> {
                    Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                }
                ScheduleAction.MORE -> {
                    Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                }
                ScheduleAction.EXAMS -> {
                    Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                }
                ScheduleAction.DELETE -> {
                    deleteWarning(activeSchedule.toSavedSchedule())
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



package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentActiveSubjectBinding
import com.bsuir.bsuirschedule.databinding.ScheduleSubjectBinding
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSubject
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.koin.androidx.navigation.koinNavGraphViewModel

class ActiveSubjectFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentActiveSubjectBinding.inflate(inflater)

        groupScheduleVM.activeSubjectStatus.observe(viewLifecycleOwner) { activeSubject ->
            if (activeSubject == null) return@observe
            val subjectManager = SubjectManager(activeSubject, requireContext())
            val subjectView = binding.nestedSubject

            subjectView.subjectBreakTime.visibility = View.GONE
            subjectView.subjectStartLesson.text = activeSubject.startLessonTime ?: "--:--"
            subjectView.subjectEndLesson.text = activeSubject.endLessonTime ?: "--:--"
            subjectView.subjectTitle.text = activeSubject.getShortTitle()
            subjectView.subjectAudience.text = activeSubject.getAudienceInLine()
            subjectManager.setSubjectTypeView(subjectView.subjectType)
            if (!activeSubject.note.isNullOrEmpty()) {
                subjectView.subjectAdditional.visibility = View.VISIBLE
                subjectView.subjectNote.text = activeSubject.getSubjectNote()
            }

            subjectView.subgroupInfo.visibility = View.VISIBLE
            if (activeSubject.numSubgroup == 0) {
                subjectView.subjectSubgroup.text = resources.getString(R.string.all_subgroups_short)
            } else {
                subjectView.subjectSubgroup.text = activeSubject.numSubgroup.toString()
            }

            if (activeSubject.employees != null) {
                val employeeNotListedText = getString(R.string.no_teacher_title)
                if (activeSubject.employees!!.size > 0) {
                    subjectView.subjectEmployeeName.text = activeSubject.employees!![0].getName()
                } else {
                    subjectView.subjectEmployeeName.text = employeeNotListedText
                }
            }
            if (activeSubject.groups != null) {
                val groupNotListedText = getString(R.string.no_group_title)
                if (activeSubject.groups!!.size > 0) {
                    subjectView.subjectEmployeeName.text = activeSubject.groups!![0].getTitleOrName()
                } else {
                    subjectView.subjectEmployeeName.text = groupNotListedText
                }
            }
        }

        return binding.root
    }

}



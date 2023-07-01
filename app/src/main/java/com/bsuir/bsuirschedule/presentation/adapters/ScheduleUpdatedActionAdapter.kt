package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleUpdatedActionBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleUpdatedAction
import com.bsuir.bsuirschedule.domain.models.ScheduleUpdatedActionsList

class ScheduleUpdatedActionAdapter(
    private val context: Context,
    private var data: ArrayList<ScheduleUpdatedAction>,
    private var isGroupSchedule: Boolean,
    private var showSubjectDialog: (action: ScheduleSubject) -> Unit,
): RecyclerView.Adapter<ScheduleUpdatedActionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleUpdatedActionBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(isGroupSchedule, showSubjectDialog, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduleDay = data[position]

        holder.bind(context, scheduleDay)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = data.size

    class ViewHolder(
        private val isGroupSchedule: Boolean,
        private val showSubjectDialog: (action: ScheduleSubject) -> Unit,
        private val binding: ScheduleUpdatedActionBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, scheduleUpdatedAction: ScheduleUpdatedAction) {
            if (scheduleUpdatedAction.items.isEmpty()) {
                binding.root.visibility = View.GONE
                return
            }

            when (scheduleUpdatedAction.action) {
                ScheduleUpdatedActionsList.ADDED_SUBJECT -> {
                    val addedActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val addedActionText = context.getString(R.string.schedule_updated_added, addedActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_add_text_color))
                    binding.actionTitle.text = addedActionText
                }
                ScheduleUpdatedActionsList.CHANGED_AUDIENCE -> {
                    val audienceActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val audienceActionText = context.getString(R.string.schedule_updated_audience, audienceActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_audience))
                    binding.actionTitle.text = audienceActionText
                }
                ScheduleUpdatedActionsList.CHANGED_GROUP -> {
                    val groupActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val groupActionText = context.getString(R.string.schedule_updated_group, groupActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_group))
                    binding.actionTitle.text = groupActionText
                }
                ScheduleUpdatedActionsList.CHANGED_EMPLOYEE -> {
                    val employeeActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val employeeActionText = context.getString(R.string.schedule_updated_employee, employeeActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_employee))
                    binding.actionTitle.text = employeeActionText
                }
                ScheduleUpdatedActionsList.CHANGED_LESSON_TYPE -> {
                    val lessonTypeActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val lessonTypeActionText = context.getString(R.string.schedule_updated_lesson_type, lessonTypeActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_subject_type))
                    binding.actionTitle.text = lessonTypeActionText
                }
                ScheduleUpdatedActionsList.CHANGED_NOTE -> {
                    val noteActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val noteActionText = context.getString(R.string.schedule_updated_note, noteActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_note_tcolor))
                    binding.actionTitle.text = noteActionText
                }
                ScheduleUpdatedActionsList.CHANGED_SUBGROUP -> {
                    val subgroupActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val subgroupActionText = context.getString(R.string.schedule_updated_subgroup, subgroupActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_subgroup))
                    binding.actionTitle.text = subgroupActionText
                }
                ScheduleUpdatedActionsList.CHANGED_WEEKS -> {
                    val weeksActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_case_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val weeksActionText = context.getString(R.string.schedule_updated_weeks, weeksActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_week_tcolor))
                    binding.actionTitle.text = weeksActionText
                }
                ScheduleUpdatedActionsList.DELETED_SUBJECT -> {
                    val deletedActionPlural = context.resources.getQuantityString(
                        R.plurals.plural_lessons,
                        scheduleUpdatedAction.items.size,
                        scheduleUpdatedAction.items.size,
                    )
                    val deletedActionText = context.getString(R.string.schedule_updated_deleted, deletedActionPlural)
                    binding.actionIcon.setImageDrawable(context.getDrawable(R.drawable.ic_delete_tcolor))
                    binding.actionTitle.text = deletedActionText
                }
            }

            val adapter = ScheduleUpdatedSubjectsAdapter(
                context,
                scheduleUpdatedAction.items,
                isGroupSchedule,
                showSubjectDialog
            )
            binding.actionsRecyclerView.adapter = adapter
            binding.actionsRecyclerView.layoutManager = LinearLayoutManager(context)
        }

    }
}



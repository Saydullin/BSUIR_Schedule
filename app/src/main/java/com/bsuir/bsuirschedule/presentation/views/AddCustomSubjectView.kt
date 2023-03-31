package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.AddCustomSubjectBinding
import com.bsuir.bsuirschedule.domain.models.*
import kotlin.collections.ArrayList

typealias OnScheduleSelect = (subject: ScheduleSubject) -> Unit

class AddCustomSubjectView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = AddCustomSubjectBinding.inflate(LayoutInflater.from(context), this)
    private var onScheduleSelect: OnScheduleSelect? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        setListeners()

        binding.nestedSubject.subjectBreakTime.visibility = View.GONE
    }

    fun setGroupType(isGroup: Boolean) {
        if (isGroup) {
            binding.groupEditText.visibility = View.VISIBLE
            binding.employeeEditText.visibility = View.GONE
        } else {
            binding.groupEditText.visibility = View.GONE
            binding.employeeEditText.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.startTimeEditText.setTextChangeListener {
            binding.nestedSubject.subjectStartLesson.text = it
        }
        binding.endTimeEditText.setTextChangeListener {
            binding.nestedSubject.subjectEndLesson.text = it
        }
        binding.shortTitleEditText.setTextChangeListener {
            binding.nestedSubject.subjectTitle.text = it
        }
        binding.audienceEditText.setTextChangeListener {
            binding.nestedSubject.subjectAudience.text = it
        }
        binding.employeeEditText.setTextChangeListener {
            binding.nestedSubject.subjectEmployeeName.text = it
        }
        binding.groupEditText.setTextChangeListener {
            binding.nestedSubject.subjectEmployeeName.text = it
        }
        binding.noteEditText.setTextChangeListener {
            if (it.isEmpty()) {
                binding.nestedSubject.subjectNote.visibility = View.GONE
            } else {
                binding.nestedSubject.subjectNote.visibility = View.VISIBLE
                binding.nestedSubject.subjectNote.text = it
            }
        }
        binding.lessonTypeAutoCompleteTextView.setOnItemClickListener { adapter, _, i, _ ->
            val item = adapter.getItemAtPosition(i)
            setLessonType(item.toString())
        }
        binding.subgroupAutoCompleteTextView.setOnItemClickListener { adapter, _, i, _ ->
            val item = adapter.getItemAtPosition(i)
            setSubgroup(item.toString())
        }

        binding.doneButton.setOnClickListener {
            val subject = getSubject()
            this.onScheduleSelect?.invoke(subject)
        }

    }

    fun setOnSelectScheduleListener(listener: OnScheduleSelect) {
        this.onScheduleSelect = listener
    }

    fun setSubgroups(subgroups: List<Int>) {
        val subgroupsText = subgroups.map {
            if (it == 0) {
                context.getString(R.string.all_subgroups_select)
            } else {
                it.toString()
            }
        }
        val arrayAdapter = ArrayAdapter(context, R.layout.dropdown_item, subgroupsText)
        binding.subgroupAutoCompleteTextView.setAdapter(arrayAdapter)
    }

    fun setWeekDays(weekDays: List<String>) {
        val arrayAdapter = ArrayAdapter(context, R.layout.dropdown_item, weekDays)
        binding.weekDayAutoCompleteTextView.setAdapter(arrayAdapter)
    }

    fun setLessonTypes(lessonTypes: List<String>) {
        val arrayAdapter = ArrayAdapter(context, R.layout.dropdown_item, lessonTypes)
        binding.lessonTypeAutoCompleteTextView.setAdapter(arrayAdapter)
    }

    fun setSubgroup(subgroup: String) {
        if (subgroup == context.getString(R.string.all_subgroups_select)) {
            binding.nestedSubject.subgroupInfo.visibility = View.GONE
        } else {
            binding.nestedSubject.subgroupInfo.visibility = View.VISIBLE
            binding.nestedSubject.subjectSubgroup.text = subgroup
        }
    }

    fun setShortTitle(shortTitle: String) {
        binding.shortTitleEditText.setText(shortTitle)
        binding.nestedSubject.subjectTitle.text = shortTitle
    }

    fun getShortTitle(): String {
        return binding.shortTitleEditText.getText()
    }

    fun getFullTitle(): String {
        return binding.fullTitleEditText.getText()
    }

    fun getStartTime(): String {
        return binding.startTimeEditText.getText()
    }

    fun getEndTime(): String {
        return binding.endTimeEditText.getText()
    }

    fun getNote(): String {
        return binding.noteEditText.getText()
    }

    fun getAudience(): String {
        return binding.audienceEditText.getText()
    }

    private fun getWeekDay(): Int {
        val weekDay = binding.weekDayAutoCompleteTextView.text.toString()
        val weekDays = listOf(
            context.getString(R.string.sunday),
            context.getString(R.string.monday),
            context.getString(R.string.tuesday),
            context.getString(R.string.wednesday),
            context.getString(R.string.thursday),
            context.getString(R.string.friday),
            context.getString(R.string.saturday)
        )

        return weekDays.indexOf(weekDay)
    }

    fun getWeeks(): ArrayList<Int> {
        val weeksList = ArrayList<Int>()

        if (binding.week1.isChecked) {
            weeksList.add(1)
        }
        if (binding.week2.isChecked) {
            weeksList.add(2)
        }
        if (binding.week3.isChecked) {
            weeksList.add(3)
        }
        if (binding.week4.isChecked) {
            weeksList.add(4)
        }

        return weeksList
    }

    fun getSubgroup(): Int {
        val selectedText = binding.subgroupAutoCompleteTextView.text.toString()

        return if (selectedText == context.getString(R.string.all_subgroups_select)) {
            0
        } else {
            selectedText.toInt()
        }
    }

    fun getSubjectType(): String {

        return when(
            binding.lessonTypeAutoCompleteTextView.text.toString()
        ) {
            context.getString(R.string.lecture) -> {
                ScheduleSubject.LESSON_TYPE_LECTURE
            }
            context.getString(R.string.practise) -> {
                ScheduleSubject.LESSON_TYPE_PRACTISE
            }
            context.getString(R.string.laboratory) -> {
                ScheduleSubject.LESSON_TYPE_LABORATORY
            }
            context.getString(R.string.consultation) -> {
                ScheduleSubject.LESSON_TYPE_CONSULTATION
            }
            context.getString(R.string.exam) -> {
                ScheduleSubject.LESSON_TYPE_EXAM
            }
            else -> {
                ScheduleSubject.LESSON_TYPE_LECTURE
            }
        }
    }

    fun setFullTitle(fullTitle: String) {
        binding.fullTitleEditText.setText(fullTitle)
    }

    fun setStartTime(startTime: String) {
        binding.startTimeEditText.setText(startTime)
        binding.nestedSubject.subjectStartLesson.text = startTime
    }

    fun setEndTime(endTime: String) {
        binding.endTimeEditText.setText(endTime)
        binding.nestedSubject.subjectEndLesson.text = endTime
    }

    fun setAudience(audience: String) {
        binding.audienceEditText.setText(audience)
        binding.nestedSubject.subjectAudience.text = audience
    }

    fun setEmployee(employeeTitle: String) {
        binding.employeeEditText.setText(employeeTitle)
        binding.nestedSubject.subjectEmployeeName.text = employeeTitle
    }

    fun setGroup(groupTitle: String) {
        binding.groupEditText.setText(groupTitle)
        binding.nestedSubject.subjectEmployeeName.text = groupTitle
    }

    private fun getSubject(): ScheduleSubject {

        return ScheduleSubject(
            id = 0,
            subject = getShortTitle(),
            subjectFullName = getFullTitle(),
            lessonTypeAbbrev = getSubjectType(),
            employees = null,
            groups = null,
            edited = null,
            subjectGroups = null,
            nextTimeDaysLeft = null,
            nextTimeSubject = null,
            startMillis = 0,
            endMillis = 0,
            startLessonTime = getStartTime(),
            endLessonTime = getEndTime(),
            numSubgroup = getSubgroup(),
            note = getNote(),
            breakTime = null,
            weekNumber = getWeeks(),
            dayNumber = getWeekDay(),
            dateLesson = "",
            startLessonDate = "",
            endLessonDate = "",
            isActual = false,
            isIgnored = false,
            audience = arrayListOf(getAudience()),
        )
    }

    fun setLessonType(lessonType: String) {
        when (lessonType) {
            context.getString(R.string.lecture) -> {
                binding.nestedSubject.subjectType.setImageDrawable(context.getDrawable(R.drawable.ic_subject_type))
                binding.nestedSubject.subjectType.setColorFilter(
                    ContextCompat.getColor(context, R.color.subject_lecture
                    )
                )
            }
            context.getString(R.string.practise) -> {
                binding.nestedSubject.subjectType.setImageDrawable(context.getDrawable(R.drawable.ic_subject_type))
                binding.nestedSubject.subjectType.setColorFilter(
                    ContextCompat.getColor(context, R.color.subject_practise
                    )
                )
            }
            context.getString(R.string.laboratory) -> {
                binding.nestedSubject.subjectType.setImageDrawable(context.getDrawable(R.drawable.ic_subject_type))
                binding.nestedSubject.subjectType.setColorFilter(
                    ContextCompat.getColor(context, R.color.subject_lab
                    )
                )
            }
            context.getString(R.string.consultation) -> {
                binding.nestedSubject.subjectType.setImageDrawable(context.getDrawable(R.drawable.ic_flag))
                binding.nestedSubject.subjectType.setColorFilter(
                    ContextCompat.getColor(context, R.color.subject_consultation
                    )
                )
            }
            context.getString(R.string.exam) -> {
                binding.nestedSubject.subjectType.setImageDrawable(context.getDrawable(R.drawable.ic_flag))
                binding.nestedSubject.subjectType.setColorFilter(
                    ContextCompat.getColor(context, R.color.subject_exam
                    )
                )
            }
        }
    }

    fun setNote(note: String) {
        binding.noteEditText.setText(note)
        if (note.isEmpty()) {
            binding.nestedSubject.subjectNote.visibility = View.GONE
        } else {
            binding.nestedSubject.subjectNote.visibility = View.VISIBLE
            binding.nestedSubject.subjectNote.text = note
        }
    }

}
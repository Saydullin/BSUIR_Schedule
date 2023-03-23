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

typealias OnScheduleSelect = () -> Unit

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

    fun setListeners() {
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
                binding.nestedSubject.subjectAdditional.visibility = View.GONE
            } else {
                binding.nestedSubject.subjectAdditional.visibility = View.VISIBLE
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
        binding.selectSchedule.setOnClickListener {
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
            binding.nestedSubject.subjectAdditional.visibility = View.GONE
        } else {
            binding.nestedSubject.subjectAdditional.visibility = View.VISIBLE
            binding.nestedSubject.subjectNote.text = note
        }
    }

}
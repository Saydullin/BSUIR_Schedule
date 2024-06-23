package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleHeaderBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.presentation.popupMenu.ScheduleHeaderMenu
import com.bumptech.glide.Glide

enum class ScheduleAction {
    DIALOG_OPEN, UPDATE, ADD_SUBJECT, SHARE, SETTINGS, UPDATE_HISTORY, EXAMS, WIDGET_ADD, DELETE
}

typealias OnScheduleActionListener = (ScheduleAction) -> Unit
typealias OnScheduleSubgroupListener = (Int) -> Unit
typealias OnScheduleTermListener = (String) -> Unit
typealias OnScheduleImageListener = () -> Unit

class ScheduleHeaderView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ScheduleHeaderBinding.inflate(LayoutInflater.from(context),this)

    private var menuListener: OnScheduleActionListener? = null
    private var subgroupListener: OnScheduleSubgroupListener? = null
    private var termListener: OnScheduleTermListener? = null
    private var imageClickListener: OnScheduleImageListener? = null
    private var isPreview = false

    private val scheduleHeaderMenu = ScheduleHeaderMenu(
        context = context,
        onUpdateClick = { this.menuListener?.invoke(ScheduleAction.UPDATE) },
        onSubjectAddClick = { this.menuListener?.invoke(ScheduleAction.ADD_SUBJECT) },
        onSettingsClick = { this.menuListener?.invoke(ScheduleAction.SETTINGS) },
        onUpdateHistoryClick = { this.menuListener?.invoke(ScheduleAction.UPDATE_HISTORY) },
        onShareClick = { this.menuListener?.invoke(ScheduleAction.SHARE) },
        onExamsClick = { this.menuListener?.invoke(ScheduleAction.EXAMS) },
        onWidgetAddClick = { this.menuListener?.invoke((ScheduleAction.WIDGET_ADD)) },
        onDeleteClick = { this.menuListener?.invoke(ScheduleAction.DELETE) },
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleHeaderView, defStyleAttr, defStyleRes)

        with (binding) {
            val imageRes = typedArray.getString(R.styleable.ScheduleHeaderView_profileImage)
            val title = typedArray.getString(R.styleable.ScheduleHeaderView_titleText)
            val subtitleText = typedArray.getString(R.styleable.ScheduleHeaderView_subTitle)
            val subgroupText = typedArray.getString(R.styleable.ScheduleHeaderView_subgroup)
            isPreview = typedArray.getBoolean(R.styleable.ScheduleHeaderView_isPreview, false)

            Glide.with(context)
                .load(imageRes)
                .placeholder(R.drawable.ic_person_placeholder)
                .into(profileImage)
            setTitle(title ?: "(empty)")
            setDescription(subtitleText ?: "(empty)")
            setSubgroupText(subgroupText ?: "(empty)")

            if (isPreview) {
                optionsButton.visibility = View.GONE
                controls.visibility = View.GONE
                scheduleHeaderDivider.visibility = View.GONE
            }
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.scheduleHeader.setOnClickListener {
            this.menuListener?.invoke(ScheduleAction.DIALOG_OPEN)
        }
        binding.profileImage.setOnClickListener {
            this.imageClickListener?.invoke()
        }
        if (!isPreview) {
            binding.optionsButton.setOnClickListener {
                initHeaderMenu(binding.optionsButton)
            }
            binding.scheduleHeader.setOnLongClickListener {
                initHeaderMenu(binding.scheduleHeader)
                true
            }
        }
        binding.scheduleSubgroupView.setSubgroupListener {
            subgroupListener?.invoke(it)
        }
        binding.scheduleTermView.setTermListener {
            termListener?.invoke(it)
        }
    }

    private fun initHeaderMenu(targetView: View) {
        scheduleHeaderMenu.initPopupMenu(targetView).show()
    }

    fun isExistExams(isExist: Boolean) {
        scheduleHeaderMenu.isExistExams(isExist)
    }

    fun setMenuListener(listener: OnScheduleActionListener) {
        this.menuListener = listener
    }

    fun setSubgroupListener(listener: OnScheduleSubgroupListener) {
        this.subgroupListener = listener
    }

    fun setTermListener(listener: OnScheduleTermListener) {
        this.termListener = listener
    }

    fun setImageClickListener(listener: OnScheduleImageListener) {
        this.imageClickListener = listener
    }

    fun setTitle(title: String) {
        binding.headerTitle.text = title
    }

    fun setSecondTitle(title: String) {
        if (title.trim().isNotBlank()) {
            binding.secondTitle.visibility = View.VISIBLE
            binding.secondTitle.text = title
        }
    }

    fun setSecondSubTitle(title: String) {
        if (title.trim().isNotBlank()) {
            binding.secondSubTitle.visibility = View.VISIBLE
            binding.secondSubTitle.text = title
        }
    }

    fun setDescription(desc: String) {
        binding.subTitle.text = desc
    }

    fun setImage(image: String) {
        Glide.with(context)
            .load(image)
            .placeholder(R.drawable.ic_person_placeholder)
            .into(binding.profileImage)
    }

    fun setImage(imageDrawable: Int) {
        Glide.with(context)
            .load(imageDrawable)
            .placeholder(R.drawable.ic_person_placeholder)
            .into(binding.profileImage)
    }

    fun setExamsIcon(isExist: Boolean) {
        if (isExist) {
            binding.examsFlag.visibility = View.VISIBLE
        } else {
            binding.examsFlag.visibility = View.GONE
        }
    }

    fun setSavedSchedule(savedSchedule: SavedSchedule) {
        if (savedSchedule.isGroup) {
            with(savedSchedule.group) {
                val courseText = resources.getString(R.string.course)
                setTitle(name)
                setSecondTitle("$course $courseText")
                setSecondSubTitle(speciality?.educationForm?.name ?: "")
                setImage(R.drawable.ic_group_placeholder)
                setDescription(getFacultyAndSpecialityAbbr())
            }
        } else {
            with(savedSchedule.employee) {
                setTitle(getTitleOrFullName())
                if (photoLink == null) {
                    setImage(R.drawable.ic_person_placeholder)
                } else {
                    setImage(photoLink)
                }
                setDescription(getDegreeAndRank())
            }
        }
        setExamsIcon(savedSchedule.isExistExams)
    }

    fun setSubgroupText(subgroupText: String) {
        binding.scheduleSubgroupView.setSubgroupText(subgroupText)
    }

    fun setTermText(termText: String) {
        binding.scheduleTermView.setTermText(termText)
    }

    fun setSubgroupItems(subgroups: List<Int>) {
        binding.scheduleSubgroupView.setSubgroups(subgroups)
    }

    fun setTermItems(terms: List<String>) {
        binding.scheduleTermView.setTerms(terms)
    }

//    fun setLocationText(locationText: String) {
//        binding.location.text = locationText
//    }

}



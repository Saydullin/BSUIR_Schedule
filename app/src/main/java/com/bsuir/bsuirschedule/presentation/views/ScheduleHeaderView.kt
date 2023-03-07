package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleHeaderBinding
import com.bsuir.bsuirschedule.presentation.popupMenu.ScheduleHeaderMenu
import com.bumptech.glide.Glide

enum class ScheduleAction {
    DIALOG_OPEN, UPDATE, SHARE, EDIT, SETTINGS, UPDATE_HISTORY, EXAMS, WIDGET_ADD, MORE, DELETE
}

typealias OnScheduleActionListener = (ScheduleAction) -> Unit

typealias OnScheduleSubgroupListener = (Int) -> Unit

class ScheduleHeaderView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ScheduleHeaderBinding.inflate(LayoutInflater.from(context),this)

    private var menuListener: OnScheduleActionListener? = null
    private var subgroupListener: OnScheduleSubgroupListener? = null
    private var isPreview = false

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
            }
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.scheduleHeader.setOnClickListener {
            this.menuListener?.invoke(ScheduleAction.DIALOG_OPEN)
        }
        if (!isPreview) {
            binding.optionsButton.setOnClickListener {
                val scheduleHeaderMenu = ScheduleHeaderMenu(
                    context = context!!,
                    onUpdateClick = { this.menuListener?.invoke(ScheduleAction.UPDATE) },
                    onEditClick = { this.menuListener?.invoke(ScheduleAction.EDIT) },
                    onSettingsClick = { this.menuListener?.invoke(ScheduleAction.SETTINGS) },
                    onUpdateHistoryClick = { this.menuListener?.invoke(ScheduleAction.UPDATE_HISTORY) },
                    onShareClick = { this.menuListener?.invoke(ScheduleAction.SHARE) },
                    onMoreClick = { this.menuListener?.invoke(ScheduleAction.MORE) },
                    onWidgetAddClick = { this.menuListener?.invoke((ScheduleAction.WIDGET_ADD)) },
                    onDeleteClick = { this.menuListener?.invoke(ScheduleAction.DELETE) },
                ).initPopupMenu(binding.optionsButton)

                scheduleHeaderMenu.show()
            }
        }
        binding.scheduleSubgroupView.setSubgroupListener {
            subgroupListener?.invoke(it)
        }
    }

    fun setMenuListener(listener: OnScheduleActionListener) {
        this.menuListener = listener
    }

    fun setSubgroupListener(listener: OnScheduleSubgroupListener) {
        this.subgroupListener = listener
    }

    fun setTitle(title: String) {
        binding.headerTitle.text = title
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

    fun setSubgroupText(subgroupText: String) {
        binding.scheduleSubgroupView.setSubgroupText(subgroupText)
    }

    fun setSubgroupItems(subgroups: List<Int>) {
        binding.scheduleSubgroupView.setSubgroups(subgroups)
    }

    fun setLocationText(locationText: String) {
        binding.location.text = locationText
    }

}



package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleHeaderBinding
import com.bsuir.bsuirschedule.presentation.popupMenu.ScheduleHeaderMenu
import com.bumptech.glide.Glide

enum class ScheduleAction {
    DIALOG_OPEN, UPDATE, SHARE, EDIT, SETTINGS, EXAMS, MORE, DELETE
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

            if (!imageRes.isNullOrEmpty()) {
                Glide.with(context)
                    .load(imageRes)
                    .placeholder(R.drawable.ic_person_placeholder)
                    .into(profileImage)
            }
            headerTitle.text = title ?: "(empty)"
            subTitle.text = subtitleText ?: "(empty)"
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.scheduleHeader.setOnClickListener {
            this.menuListener?.invoke(ScheduleAction.DIALOG_OPEN)
        }
        binding.optionsButton.setOnClickListener {
            val scheduleHeaderMenu = ScheduleHeaderMenu(
                context = context!!,
                onUpdateClick = { this.menuListener?.invoke(ScheduleAction.UPDATE) },
                onEditClick = { this.menuListener?.invoke(ScheduleAction.EDIT) },
                onSettingsClick = { this.menuListener?.invoke(ScheduleAction.SETTINGS) },
                onShareClick = { this.menuListener?.invoke(ScheduleAction.SHARE) },
                onMoreClick = { this.menuListener?.invoke(ScheduleAction.MORE) },
                onDeleteClick = { this.menuListener?.invoke(ScheduleAction.DELETE) },
            ).initPopupMenu(binding.optionsButton)

            scheduleHeaderMenu.show()
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

    fun setImageDrawable(imageDrawable: Int) {
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



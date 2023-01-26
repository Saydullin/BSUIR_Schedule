package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleHeaderBinding
import com.bumptech.glide.Glide

enum class ScheduleAction {
    DIALOG_OPEN, UPDATE, EDIT, SETTINGS, EXAMS, DELETE
}

typealias OnScheduleActionListener = (ScheduleAction) -> Unit

class ScheduleHeaderView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ScheduleHeaderBinding.inflate(LayoutInflater.from(context),this)

    private var menuListener: OnScheduleActionListener? = null

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
            // Set static text
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.scheduleHeader.setOnClickListener {
            this.menuListener?.invoke(ScheduleAction.DIALOG_OPEN)
        }
    }

    fun setMenuListener(listener: OnScheduleActionListener) {
        this.menuListener = listener
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
        binding.subgroupText.text = subgroupText
    }

    fun setLocationText(locationText: String) {
        binding.location.text = locationText;
    }

}



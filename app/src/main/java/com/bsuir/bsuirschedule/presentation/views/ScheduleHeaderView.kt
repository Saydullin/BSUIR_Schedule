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
) : LinearLayout(context, attrs) {

    private val binding: ScheduleHeaderBinding

    private var menuListener: OnScheduleActionListener? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.schedule_header, this, true)
        binding = ScheduleHeaderBinding.bind(this)
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
            titleText.text = title ?: "(empty)"
            subTitle.text = subtitleText ?: "(empty)"
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.root.setOnClickListener { 
            this.menuListener?.invoke(ScheduleAction.DIALOG_OPEN)
        }
    }

    fun setMenuListener(listener: OnScheduleActionListener) {
        this.menuListener = listener
    }

}



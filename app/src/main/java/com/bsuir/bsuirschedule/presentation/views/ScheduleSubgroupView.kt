package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleSubgroupBinding

enum class ScheduleSubjectAction {
    SUBGROUP_CHANGE
}

typealias OnSubgroupSelectListener = (Int) -> Unit

class ScheduleSubgroupView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ScheduleSubgroupBinding.inflate(LayoutInflater.from(context), this)
    private val popupMenu = PopupMenu(context, binding.root)
    private var subgroupListener: OnSubgroupSelectListener ?= null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleSubgroupView, defStyleAttr, defStyleRes)

        with (binding) {
            val subgroupNum = typedArray.getString(R.styleable.ScheduleSubgroupView_subgroupNum)

            subgroupNumber.text = subgroupNum
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.root.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener {
            binding.subgroupNumber.text = it.title
            if (it.title == context.getString(R.string.all_subgroups_select)) {
                subgroupListener?.invoke(0)
                binding.subgroupNumber.text = context.getString(R.string.all_subgroups_short)
            } else {
                subgroupListener?.invoke(it.title.toString().toInt())
            }
            true
        }
    }

    fun setSubgroupListener(listener: OnSubgroupSelectListener) {
        this.subgroupListener = listener
    }

    fun setSubgroups(subgroups: List<Int> ) {
        popupMenu.menu.clear()
        subgroups.forEach {
            if (it == 0) {
                popupMenu.menu.add(context.getString(R.string.all_subgroups_select))
            } else {
                popupMenu.menu.add(it.toString())
            }
        }
    }

    fun setSubgroupText(subgroupText: String) {
        binding.subgroupNumber.text = subgroupText
    }

}


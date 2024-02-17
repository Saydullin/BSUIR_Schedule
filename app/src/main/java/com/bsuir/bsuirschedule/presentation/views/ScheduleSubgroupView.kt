package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleSubgroupBinding

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
            if (it.title == context.getString(R.string.all_subgroups_short)) {
                subgroupListener?.invoke(0)
                binding.subgroupNumber.text = context.getString(R.string.all_subgroups_short)
            } else {
                subgroupListener?.invoke(it.itemId)
                binding.subgroupNumber.text = it.itemId.toString()
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
                popupMenu.menu.add(context.getString(R.string.all_subgroups_short))
            } else {
                val subgroupText = context.getString(R.string.settings_item_subgroup, it)
                popupMenu.menu.add(0, it, it, subgroupText)
            }
        }
    }

    fun setTerms(terms: List<Int> ) {
        popupMenu.menu.clear()
        terms.forEach {
            if (it == 0) {
                popupMenu.menu.add(context.getString(R.string.all_subgroups_short))
            } else {
                val termText = context.getString(R.string.settings_item_subgroup, it)
                popupMenu.menu.add(0, it, it, termText)
            }
        }
    }

    fun setSubgroupText(subgroupText: String) {
        binding.subgroupNumber.text = subgroupText
    }

}



package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleTermBinding

typealias OnTermSelectListener = (Int) -> Unit

class ScheduleTermView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ScheduleTermBinding.inflate(LayoutInflater.from(context), this)
    private val popupMenu = PopupMenu(context, binding.root)
    private var termListener: OnTermSelectListener ?= null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleTermView, defStyleAttr, defStyleRes)

        with (binding) {
            val termText = typedArray.getString(R.styleable.ScheduleTermView_term)

            term.text = termText
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.root.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener {
            if (it.title == context.getString(R.string.all_subgroups_short)) {
                termListener?.invoke(0)
                binding.term.text = context.getString(R.string.all_subgroups_short)
            } else {
                termListener?.invoke(it.itemId)
                binding.term.text = it.itemId.toString()
            }
            true
        }
    }

    fun setTermListener(listener: OnSubgroupSelectListener) {
        this.termListener = listener
    }

    fun setTerms(terms: List<String> ) {
        popupMenu.menu.clear()
        terms.forEach {
            popupMenu.menu.add(it)

        }
    }

    fun setTermText(termText: String) {
        binding.term.text = termText
    }

}



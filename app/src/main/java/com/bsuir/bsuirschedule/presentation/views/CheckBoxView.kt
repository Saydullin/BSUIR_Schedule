package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.children
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.CheckboxViewBinding
import com.bumptech.glide.Glide

typealias OnCheckedListener = (isChecked: Boolean) -> Unit

class CheckBoxView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = CheckboxViewBinding.inflate(LayoutInflater.from(context), this)
    private var onCheckedListener: OnCheckedListener? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
        setListeners()

        orientation = VERTICAL
        children
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxView, defStyleAttr, defStyleRes)

        with(binding) {
            val icon = typedArray.getDrawable(R.styleable.CheckBoxView_checkbox_icon)
            val title = typedArray.getString(R.styleable.CheckBoxView_checkbox_title) ?: ""
            val description = typedArray.getString(R.styleable.CheckBoxView_checkbox_description) ?: ""
            val caption = typedArray.getString(R.styleable.CheckBoxView_checkbox_caption) ?: ""

            setTitle(title)
            setDescription(description)
            setCaption(caption)
            setIcon(icon)
        }

        typedArray.recycle()
    }

    private fun setListeners() {
        binding.root.setOnClickListener {
            binding.checkbox.toggle()
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onCheckedListener?.invoke(isChecked)
        }
    }

    fun setOnCheckListener(listener: OnCheckedListener) {
        this.onCheckedListener = listener
    }

    fun setChecked(isChecked: Boolean) {
        binding.checkbox.isChecked = isChecked
    }

    fun isChecked(): Boolean {
        return binding.checkbox.isChecked
    }

    fun setIcon(icon: Drawable?) {
        Glide.with(context)
            .load(icon)
            .into(binding.checkboxIcon)
    }

    fun setTitle(title: String) {
        binding.checkboxTitle.text = title
    }

    fun setDescription(description: String) {
        binding.checkboxDescription.text = description
    }

    fun setCaption(caption: String) {
        binding.checkbox.text = caption
    }

}
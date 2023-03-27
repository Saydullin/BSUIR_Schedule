package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.AboutInfoViewBinding

class AboutInfoView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = AboutInfoViewBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AboutInfoView, defStyleAttr, defStyleRes)

        val icon = typedArray.getDrawable(R.styleable.AboutInfoView_about_icon)
        val title = typedArray.getString(R.styleable.AboutInfoView_about_title) ?: ""
        val text = typedArray.getString(R.styleable.AboutInfoView_about_text) ?: ""

        setDrawable(icon)
        setTitle(title)
        setText(text)
    }

    fun setDrawable(drawable: Drawable?) {
        binding.aboutIcon.setImageDrawable(drawable)
    }

    fun setTitle(title: String) {
        binding.aboutTitle.text = title
    }

    fun setText(text: String) {
        binding.aboutText.text = text
    }

}
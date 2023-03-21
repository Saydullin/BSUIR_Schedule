package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.EditTextViewBinding

typealias EditTextChangeListener = (String) -> Unit

class EditTextView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = EditTextViewBinding.inflate(LayoutInflater.from(context),this)
    private var editTextChangeListener: EditTextChangeListener? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
        setListeners()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextView, defStyleAttr, defStyleRes)

        val hint = typedArray.getString(R.styleable.EditTextView_hint) ?: ""
        val caption = typedArray.getString(R.styleable.EditTextView_caption) ?: ""
        setHint(hint)
        setCaption(caption)
    }

    private fun setListeners() {
        binding.editTextInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                editTextChangeListener?.invoke(text.toString().trim())
            }
        })
    }

    fun setTextChangeListener(listener: EditTextChangeListener) {
        this.editTextChangeListener = listener
    }

    fun getText(): String {
        return binding.editTextInput.text.toString().trim()
    }

    fun setCaption(caption: String) {
        binding.caption.text = caption
        if (caption.isNotEmpty()) {
            binding.caption.visibility = View.VISIBLE
        } else {
            binding.caption.visibility = View.GONE
        }
    }

    fun setHint(hint: String) {
        binding.editTextInput.hint = hint
    }

}
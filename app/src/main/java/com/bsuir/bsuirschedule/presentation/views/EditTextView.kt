package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.EditTextViewBinding
import com.bsuir.bsuirschedule.domain.models.Time
import com.bsuir.bsuirschedule.presentation.dialogs.TimePickerDialog

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

    companion object {
        const val TIME_PICKER_ACTION = "time"
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextView, defStyleAttr, defStyleRes)

        val hint = typedArray.getString(R.styleable.EditTextView_hint) ?: ""
        val caption = typedArray.getString(R.styleable.EditTextView_caption) ?: ""
        val maxLength = typedArray.getInt(R.styleable.EditTextView_maxLength, 0)
        val inputType = typedArray.getInt(R.styleable.EditTextView_android_inputType, EditorInfo.TYPE_NULL)
        val action = typedArray.getString(R.styleable.EditTextView_action) ?: ""

        if (inputType != EditorInfo.TYPE_NULL) {
            setInputType(inputType)
        }
        setAction(action)
        setHint(hint)
        setCaption(caption)
        setMaxLength(maxLength)

        typedArray.recycle()
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

    fun setText(text: String) {
        binding.editTextInput.setText(text)
    }

    fun length(): Int {
        return binding.editTextInput.length()
    }

    fun setSelection(index: Int) {
        binding.editTextInput.setSelection(index)
    }

    fun setCaption(caption: String) {
        binding.caption.text = caption
        if (caption.isNotEmpty()) {
            binding.caption.visibility = View.VISIBLE
        } else {
            binding.caption.visibility = View.GONE
        }
    }

    fun setMaxLength(length: Int) {
        if (length != 0) {
            binding.editTextInput.filters = arrayOf(InputFilter.LengthFilter(length))
        }
    }

    fun setHint(hint: String) {
        binding.editTextInput.hint = hint
    }

    fun setType(inputType: Int) {
        binding.editTextInput.inputType = inputType
    }

    fun setAction(action: String) {
        when(action) {
            TIME_PICKER_ACTION -> {
                setTimePickerMode()
            }
        }
    }

    fun setInputType(inputType: Int) {
        binding.editTextInput.inputType = inputType
    }

    fun isFocusable(isFocusable: Boolean) {
        binding.editTextInput.isFocusable = isFocusable
    }

    fun clearInputFocus() {
        binding.editTextInput.clearFocus()
    }

    fun isSaveEnabled(isSaveEnabled: Boolean) {
        binding.editTextInput.isSaveEnabled = isSaveEnabled
    }

    private val onSelectedTime = { time: Time ->
        val hour = if (time.hour >= 10) {
            time.hour.toString()
        } else {
            "0${time.hour}"
        }
        val minute = if (time.minute >= 10) {
            time.minute.toString()
        } else {
            "0${time.minute}"
        }
        val timeText = "$hour:$minute"

        setText(timeText)
    }

    private fun setTimePickerMode() {
        binding.editTextInput.gravity = Gravity.CENTER

        setText("00:00")
        isFocusable(false)

        binding.editTextInput.setOnClickListener {
            val timePickerDialog = TimePickerDialog(context, onSelectedTime)
            timePickerDialog.show()
        }
    }

}



package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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
        val editTextType = typedArray.getString(R.styleable.EditTextView_type) ?: ""
        val action = typedArray.getString(R.styleable.EditTextView_action) ?: ""
        setAction(action)
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

    fun setText(text: String) {
        binding.editTextInput.setText(text)
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
        binding.editTextInput.setText("00:00")
        binding.editTextInput.gravity = Gravity.CENTER

        binding.editTextInput.isFocusable = false

        binding.editTextInput.setOnClickListener {
            val timePickerDialog = TimePickerDialog(context, onSelectedTime)
            timePickerDialog.show()
        }
    }

}



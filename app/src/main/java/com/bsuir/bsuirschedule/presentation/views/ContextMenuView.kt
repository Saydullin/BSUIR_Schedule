package com.bsuir.bsuirschedule.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.bsuir.bsuirschedule.databinding.ContextMenuBinding

class ContextMenuView(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int,
    defStyleRes: Int,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ContextMenuBinding.inflate(LayoutInflater.from(context), this)

}


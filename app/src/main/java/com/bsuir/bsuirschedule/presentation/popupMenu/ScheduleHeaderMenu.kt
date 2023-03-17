package com.bsuir.bsuirschedule.presentation.popupMenu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.bsuir.bsuirschedule.R

class ScheduleHeaderMenu(
    private val context: Context,
    private val onUpdateClick: () -> Unit,
    private val onSubjectAddClick: () -> Unit,
    private val onSettingsClick: () -> Unit,
    private val onUpdateHistoryClick: () -> Unit,
    private val onShareClick: () -> Unit,
    private val onWidgetAddClick: () -> Unit,
    private val onDeleteClick: () -> Unit
) {

    fun initPopupMenu(targetView: View): PopupMenu {
        val popupMenu = PopupMenu(context, targetView)

        popupMenu.inflate(R.menu.schedule_header_menu)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.update -> {
                    onUpdateClick()
                    true
                }
                R.id.add_subject -> {
                    onSubjectAddClick()
                    true
                }
                R.id.settings -> {
                    onSettingsClick()
                    true
                }
                R.id.update_history -> {
                    onUpdateHistoryClick()
                    true
                }
                R.id.share -> {
                    onShareClick()
                    true
                }
                R.id.delete -> {
                    onDeleteClick()
                    true
                }
                R.id.add_widget -> {
                    onWidgetAddClick()
                    true
                }
                else -> {
                    true
                }
            }
        }

        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)

        return popupMenu
    }

}



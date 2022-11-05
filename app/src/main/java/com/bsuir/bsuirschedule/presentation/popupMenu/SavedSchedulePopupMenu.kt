package com.bsuir.bsuirschedule.presentation.popupMenu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class SavedSchedulePopupMenu(
    private val context: Context,
    private val savedSchedule: SavedSchedule,
    private val update: (savedSchedule: SavedSchedule) -> Unit,
    private val delete: (savedSchedule: SavedSchedule) -> Unit,
    private val rename: (savedSchedule: SavedSchedule) -> Unit,
) {

    fun initPopupMenu(targetView: View): PopupMenu {
        val popupMenu = PopupMenu(context, targetView)

        popupMenu.inflate(R.menu.saved_schedule_menu)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.update -> {
                    update(savedSchedule)
                    true
                }
                R.id.delete -> {
                    delete(savedSchedule)
                    true
                }
                R.id.rename -> {
                    rename(savedSchedule)
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



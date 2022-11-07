package com.bsuir.bsuirschedule.presentation.popupMenu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

class ScheduleSubjectPopupMenu(
    private val context: Context,
    private val scheduleSubject: ScheduleSubject,
    private val edit: (scheduleSubject: ScheduleSubject) -> Unit,
    private val isIgnore: (scheduleSubject: ScheduleSubject, isIgnore: Boolean) -> Unit,
    private val delete: (scheduleSubject: ScheduleSubject) -> Unit,
) {

    fun initPopupMenu(targetView: View): PopupMenu {
        val popupMenu = PopupMenu(context, targetView)

        popupMenu.inflate(R.menu.schedule_subject_menu)

        if (scheduleSubject.isIgnored == true) {
            popupMenu.menu.removeItem(R.id.deactivate_subject)
        } else {
            popupMenu.menu.removeItem(R.id.activate_subject)
        }

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.activate_subject -> {
                    isIgnore(scheduleSubject, false)
                    true
                }
                R.id.deactivate_subject -> {
                    isIgnore(scheduleSubject, true)
                    true
                }
                R.id.edit_subject -> {
                    edit(scheduleSubject)
                    true
                }
                R.id.delete_subject -> {
                    delete(scheduleSubject)
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



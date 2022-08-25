package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentScheduleSavedItemsBinding
import com.example.bsuirschedule.domain.models.LoadingStatus
import com.example.bsuirschedule.domain.models.SavedSchedule
import com.example.bsuirschedule.presentation.adapters.SavedItemsAdapter
import com.example.bsuirschedule.presentation.dialogs.LoadingDialog
import com.example.bsuirschedule.presentation.dialogs.StateDialog
import com.example.bsuirschedule.presentation.popupMenu.SavedSchedulePopupMenu
import com.example.bsuirschedule.presentation.utils.FilterManager
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.example.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

// Saved items
class ScheduleSavedItemsFragment : Fragment() {

    private val savedScheduleVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSavedItemsBinding.inflate(inflater)
        val filterCallback = { s: String, isAsc: Boolean ->
            savedScheduleVM.filterByKeyword(s, isAsc)
        }
        val filterManager = FilterManager(
            binding.nestedFilter,
            filterCallback,
            false
        )
        filterManager.init()

        binding.scheduleSavedItemsRecycler.layoutManager = LinearLayoutManager(context)

        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val dialog = LoadingDialog(loadingStatus)
        dialog.isCancelable = false

        val getGroupScheduleLambda = { savedSchedule: SavedSchedule ->
            groupScheduleVM.selectSchedule(savedSchedule)
            Navigation.findNavController(binding.root).navigate(R.id.action_to_main_schedules)
        }

        val deleteSchedule = { savedSchedule: SavedSchedule ->
            savedScheduleVM.deleteSchedule(savedSchedule)
            groupScheduleVM.deleteSchedule(savedSchedule)
        }

        val updateSchedule = { savedSchedule: SavedSchedule ->
            savedScheduleVM.setActiveSchedule(savedSchedule)
            if (savedSchedule.isGroup) {
                groupScheduleVM.getGroupScheduleAPI(savedSchedule.group)
            } else {
                groupScheduleVM.getEmployeeScheduleAPI(savedSchedule.employee)
            }
        }

        val longPressLambda = { savedSchedule: SavedSchedule, view: View ->
            val popupMenu = SavedSchedulePopupMenu(
                context!!,
                savedSchedule = savedSchedule,
                delete = deleteSchedule,
                update = updateSchedule
            ).initPopupMenu(view)

            popupMenu.show()
        }

        groupScheduleVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                groupScheduleVM.closeError()
            }
        }

        groupScheduleVM.loadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                dialog.show(parentFragmentManager, "LoadingDialog")
            } else {
                if (dialog.dialog?.isShowing == true) {
                    dialog.dismiss()
                }
            }
        }

        savedScheduleVM.errorMessageStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                savedScheduleVM.closeError()
            }
        }

        savedScheduleVM.savedSchedulesStatus.observe(viewLifecycleOwner) { savedSchedules ->
            if (savedSchedules == null) {
                binding.hiddenPlaceholder.visibility = View.VISIBLE
                binding.savedSchedules.visibility = View.GONE
            } else {
                binding.savedSchedules.visibility = View.VISIBLE
                binding.hiddenPlaceholder.visibility = View.GONE
                val pluralSchedules = resources.getQuantityString(R.plurals.plural_schedules, savedSchedules.size, savedSchedules.size)
                binding.nestedFilter.filterAmount.text = pluralSchedules
                val adapter = SavedItemsAdapter(context!!, savedSchedules, getGroupScheduleLambda, longPressLambda)
                binding.scheduleSavedItemsRecycler.adapter = adapter
                binding.scheduleSavedItemsRecycler.alpha = 0f
                binding.scheduleSavedItemsRecycler.animate().alpha(1f).setDuration(300).start()
            }
        }

        return binding.root
    }

}



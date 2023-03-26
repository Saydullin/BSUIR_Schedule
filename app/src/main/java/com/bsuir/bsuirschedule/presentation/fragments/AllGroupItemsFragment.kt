package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentAllGroupItemsBinding
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.adapters.GroupItemsAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.LoadingDialog
import com.bsuir.bsuirschedule.presentation.dialogs.ScheduleItemPreviewDialog
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class AllGroupItemsFragment : Fragment() {

    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val savedItemsVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAllGroupItemsBinding.inflate(inflater)

        val saveGroupCallback = { savedSchedule: SavedSchedule ->
            if (savedSchedule.isGroup) {
                groupScheduleVM.getGroupScheduleAPI(savedSchedule.group)
            }
        }
        val showScheduleCallback = { savedSchedule: SavedSchedule ->
            Navigation.findNavController(binding.root).popBackStack()
            Navigation.findNavController(binding.root).popBackStack()
            groupScheduleVM.selectSchedule(savedSchedule.id)
        }
        val selectGroupLambda = { group: Group ->
            val stateDialog = ScheduleItemPreviewDialog(
                group.toSavedSchedule(false),
                saveGroupCallback,
                showScheduleCallback,
                group.isSaved
            )
            stateDialog.isCancelable = true
            stateDialog.show(parentFragmentManager, "employeePreview")
        }

        binding.nestedFilter.editText.isSaveEnabled(false)
        binding.nestedFilter.editText.setTextChangeListener {
            groupItemsVM.filterByKeyword(it, true)
        }

        val adapter = GroupItemsAdapter(requireContext(), ArrayList(), selectGroupLambda)
        binding.scheduleGroupItemsRecycler.layoutManager = LinearLayoutManager(context)
        binding.scheduleGroupItemsRecycler.adapter = adapter

        binding.refreshScheduleItems.setDistanceToTriggerSync(500)
        binding.refreshScheduleItems.setOnRefreshListener {
            groupItemsVM.updateInitDataAndGroups()
        }

        groupItemsVM.isUpdatingStatus.observe(viewLifecycleOwner) { isUpdating ->
            binding.refreshScheduleItems.isRefreshing = isUpdating
        }

        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val dialog = LoadingDialog(loadingStatus)
        dialog.isCancelable = false

        groupScheduleVM.scheduleLoadedStatus.observe(viewLifecycleOwner) { savedSchedule ->
            if (savedSchedule == null || !savedSchedule.isGroup) return@observe
            savedSchedule.group.isSaved = true
            groupItemsVM.saveGroupItem(savedSchedule.group)
            savedItemsVM.saveSchedule(savedSchedule)
            adapter.setSavedItem(savedSchedule.group)
            groupScheduleVM.setScheduleLoadedNull()
        }

        groupItemsVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                groupItemsVM.closeError()
            }
        }

        groupScheduleVM.groupLoadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading == null) return@observe
            if (loading) {
                dialog.show(parentFragmentManager, "LoadingDialog")
            } else {
                if (dialog.dialog?.isShowing == true) {
                    dialog.dismiss()
                }
            }
        }

        groupScheduleVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                groupScheduleVM.closeError()
            }
        }

        groupItemsVM.allGroupItemsStatus.observe(viewLifecycleOwner) { groupItems ->
            if (groupItems == null) {
                binding.placeholder.visibility = View.VISIBLE
                binding.content.visibility = View.GONE
            } else {
                val pluralSchedules = resources.getQuantityString(R.plurals.plural_groups, groupItems.size, groupItems.size)
                binding.placeholder.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
                binding.nestedFilter.filterAmount.text = pluralSchedules
                adapter.setGroupList(groupItems)
                binding.scheduleGroupItemsRecycler.alpha = 0f
                binding.scheduleGroupItemsRecycler.animate().alpha(1f).setDuration(300).start()
            }
        }

        return binding.root
    }

}



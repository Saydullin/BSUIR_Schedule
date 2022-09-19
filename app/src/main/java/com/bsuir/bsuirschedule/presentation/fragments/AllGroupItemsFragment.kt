package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentAllGroupItemsBinding
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.presentation.adapters.GroupItemsAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.LoadingDialog
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.utils.FilterManager
import com.bsuir.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class AllGroupItemsFragment : Fragment() {

    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAllGroupItemsBinding.inflate(inflater)
        val filterCallback = { s: String, isAsc: Boolean ->
            groupItemsVM.filterByKeyword(s, isAsc)
        }
        val filterManager = FilterManager(binding.nestedFilter, filterCallback)
        filterManager.init()

        binding.refreshScheduleItems.setDistanceToTriggerSync(500)
        binding.refreshScheduleItems.setOnRefreshListener {
            groupItemsVM.updateGroupItems()
        }

        groupItemsVM.isUpdatingStatus.observe(viewLifecycleOwner) { isUpdating ->
            binding.refreshScheduleItems.isRefreshing = isUpdating
        }

        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val dialog = LoadingDialog(loadingStatus)
        dialog.isCancelable = false

        val saveGroupLambda = { group: Group ->
            groupScheduleVM.getGroupScheduleAPI(group)
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

        groupItemsVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                groupItemsVM.closeError()
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
            if (groupItems.isEmpty()) {
                binding.placeholder.visibility = View.VISIBLE
                binding.content.visibility = View.GONE
            } else {
                binding.placeholder.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
                val pluralSchedules = resources.getQuantityString(R.plurals.plural_groups, groupItems.size, groupItems.size)
                binding.nestedFilter.filterAmount.text = pluralSchedules
                binding.scheduleItemsRecycler.layoutManager = LinearLayoutManager(context)
                binding.scheduleItemsRecycler.adapter = GroupItemsAdapter(context!!, groupItems, saveGroupLambda)
                binding.scheduleItemsRecycler.alpha = 0f
                binding.scheduleItemsRecycler.animate().alpha(1f).setDuration(300).start()
            }
        }

        return binding.root
    }

}



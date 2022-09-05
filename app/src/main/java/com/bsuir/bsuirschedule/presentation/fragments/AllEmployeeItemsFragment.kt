package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentAllEmployeeItemsBinding
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.presentation.adapters.EmployeeItemsAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.LoadingDialog
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.utils.FilterManager
import com.bsuir.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class AllEmployeeItemsFragment : Fragment() {

    private val groupSchedule: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAllEmployeeItemsBinding.inflate(inflater)
        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val dialog = LoadingDialog(loadingStatus)
        dialog.isCancelable = false

        val filterCallback = { s: String, isAsc: Boolean ->
            employeeItemsVM.filterByKeyword(s, isAsc)
        }
        val filterManager = FilterManager(binding.nestedFilter, filterCallback)
        filterManager.init()

        binding.refreshScheduleItems.setDistanceToTriggerSync(500)
        binding.refreshScheduleItems.setOnRefreshListener {
            employeeItemsVM.updateEmployeeItems()
        }

        employeeItemsVM.isUpdatingStatus.observe(viewLifecycleOwner) { isUpdated ->
            binding.refreshScheduleItems.isRefreshing = isUpdated
        }

        val saveEmployeeLambda = { employee: Employee ->
            groupSchedule.getEmployeeScheduleAPI(employee)
        }

        groupSchedule.employeeLoadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading == null) return@observe
            if (loading) {
                dialog.show(parentFragmentManager, "LoadingDialog")
            } else {
                if (dialog.dialog?.isShowing == true) {
                    dialog.dismiss()
                }
            }
        }

        employeeItemsVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                Log.e("sady", "employee items error: ${errorStatus.message}")
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                employeeItemsVM.closeError()
            }
        }

        groupSchedule.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                Log.e("sady", "groupSchedule error: ${errorStatus.message}")
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
            }
        }

        employeeItemsVM.employeeItemsStatus.observe(viewLifecycleOwner) { employeeItems ->
            val pluralSchedules = resources.getQuantityString(R.plurals.plural_employees, employeeItems.size, employeeItems.size)
            binding.nestedFilter.filterAmount.text = pluralSchedules
            binding.scheduleItemsRecycler.layoutManager = LinearLayoutManager(context)
            binding.scheduleItemsRecycler.adapter = EmployeeItemsAdapter(context!!, employeeItems, saveEmployeeLambda)
            binding.scheduleItemsRecycler.alpha = 0f
            binding.scheduleItemsRecycler.animate().alpha(1f).setDuration(300).start()
        }

        return binding.root
    }

}
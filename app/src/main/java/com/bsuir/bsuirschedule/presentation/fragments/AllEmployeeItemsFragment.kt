package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentAllEmployeeItemsBinding
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.adapters.EmployeeItemsAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.LoadingDialog
import com.bsuir.bsuirschedule.presentation.dialogs.ScheduleItemPreviewDialog
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class AllEmployeeItemsFragment : Fragment() {

    private val scheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val savedItemsVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAllEmployeeItemsBinding.inflate(inflater)
        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val dialog = LoadingDialog(loadingStatus)
        dialog.isCancelable = false

        binding.nestedFilter.editText.isSaveEnabled(false)
        binding.nestedFilter.editText.setTextChangeListener {
            employeeItemsVM.filterByKeyword(it, true)
        }

        binding.refreshScheduleItems.setDistanceToTriggerSync(500)
        binding.refreshScheduleItems.setOnRefreshListener {
            employeeItemsVM.updateDepartmentsAndEmployeeItems()
        }

        employeeItemsVM.isUpdatingStatus.observe(viewLifecycleOwner) { isUpdated ->
            binding.refreshScheduleItems.isRefreshing = isUpdated
        }

        val saveEmployeeCallback = { savedSchedule: SavedSchedule ->
            if (!savedSchedule.isGroup) {
                scheduleVM.getEmployeeScheduleAPI(savedSchedule.employee)
            }
        }
        val showScheduleCallback = { savedSchedule: SavedSchedule ->
            Navigation.findNavController(binding.root).popBackStack() // FIXME Clear stack, navigate main
            Navigation.findNavController(binding.root).popBackStack()
            scheduleVM.selectSchedule(savedSchedule.id)
        }
        val selectEmployeeCallback = { employee: Employee ->
            val stateDialog = ScheduleItemPreviewDialog(
                employee.toSavedSchedule(false),
                saveEmployeeCallback,
                showScheduleCallback,
                employee.isSaved
            )
            stateDialog.isCancelable = true
            stateDialog.show(parentFragmentManager, "employeePreview")
        }

        val adapter = EmployeeItemsAdapter(requireContext(), ArrayList(), selectEmployeeCallback)
        binding.scheduleEmployeeItemsRecycler.layoutManager = LinearLayoutManager(context)
        binding.scheduleEmployeeItemsRecycler.adapter = adapter

        scheduleVM.employeeLoadingStatus.observe(viewLifecycleOwner) { loading ->
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
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                employeeItemsVM.closeError()
            }
        }

        scheduleVM.scheduleLoadedStatus.observe(viewLifecycleOwner) { savedSchedule ->
            if (savedSchedule == null || savedSchedule.isGroup) return@observe
            savedSchedule.employee.isSaved = true
            savedItemsVM.saveSchedule(savedSchedule)
            employeeItemsVM.saveEmployeeItem(savedSchedule.employee)
            scheduleVM.setScheduleLoadedNull()
            adapter.setSavedItem(savedSchedule.employee)
        }

        employeeItemsVM.employeeItemsStatus.observe(viewLifecycleOwner) { employeeItems ->
            if (employeeItems == null) {
                binding.placeholder.visibility = View.VISIBLE
                binding.content.visibility = View.GONE
            } else {
                binding.placeholder.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
                val pluralSchedules = resources.getQuantityString(R.plurals.plural_employees, employeeItems.size, employeeItems.size)
                adapter.setEmployeeList(employeeItems)
                binding.nestedFilter.filterAmount.text = pluralSchedules
                binding.scheduleEmployeeItemsRecycler.alpha = 0f
                binding.scheduleEmployeeItemsRecycler.animate().alpha(1f).setDuration(300).start()
            }
        }

        return binding.root
    }

}



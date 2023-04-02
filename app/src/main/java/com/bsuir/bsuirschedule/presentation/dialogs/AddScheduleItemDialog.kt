package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.AddScheduleItemDialogBinding
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.adapters.EmployeeItemsAdapter
import com.bsuir.bsuirschedule.presentation.adapters.GroupItemsAdapter
import com.bsuir.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class AddScheduleItemDialog(
    private val isGroup: Boolean,
    private val onSelectScheduleItem: (savedSchedule: SavedSchedule) -> Unit,
) : DialogFragment() {

    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val binding = AddScheduleItemDialogBinding.inflate(inflater)
        val recyclerViewLayoutManager = LinearLayoutManager(context)

        val onSelectGroupItem = { group: Group ->
            onSelectScheduleItem(group.toSavedSchedule())
            dismiss()
        }

        val onSelectEmployeeItem = { employee: Employee ->
            onSelectScheduleItem(employee.toSavedSchedule())
            dismiss()
        }

        binding.searchSourceSchedule.setTextChangeListener {
            if (isGroup) {
                groupItemsVM.filterByKeyword(it)
            } else {
                employeeItemsVM.filterByKeyword(it)
            }
        }

        if (isGroup) {
            groupItemsVM.getAllGroupItems()
            val adapter = GroupItemsAdapter(requireContext(), arrayListOf(), onSelectGroupItem)
            binding.sourceRecycler.layoutManager = recyclerViewLayoutManager
            binding.sourceRecycler.adapter = adapter
            groupItemsVM.allGroupItemsStatus.observe(viewLifecycleOwner) { groupItems ->
                if (groupItems != null) {
                    adapter.setGroupList(groupItems)
                }
            }
        } else {
            employeeItemsVM.getAllEmployeeItems()
            val adapter = EmployeeItemsAdapter(requireContext(), arrayListOf(), onSelectEmployeeItem)
            binding.sourceRecycler.layoutManager = recyclerViewLayoutManager
            binding.sourceRecycler.adapter = adapter
            employeeItemsVM.employeeItemsStatus.observe(viewLifecycleOwner) { employeeItems ->
                if (employeeItems != null) {
                    adapter.setEmployeeList(employeeItems)
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}
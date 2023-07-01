package com.bsuir.bsuirschedule.presentation.fragments

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentMainScheduleBinding
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.utils.ErrorMessage
import com.bsuir.bsuirschedule.presentation.viewModels.*
import com.bsuir.bsuirschedule.presentation.widgets.ScheduleWidget
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

class MainScheduleFragment : Fragment(), KoinComponent {

    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val savedItemsVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val currentWeekVM: CurrentWeekViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()
    private lateinit var binding: FragmentMainScheduleBinding

    override fun onResume() {
        super.onResume()

        groupScheduleVM.updateSchedule()
    }

    override fun onPause() {
        super.onPause()

        groupScheduleVM.setUpdateStatus(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScheduleBinding.inflate(inflater)
        val weekText = getString(R.string.week)
        val datePattern = SimpleDateFormat("E d MMMM")
        val calendar = Calendar.getInstance(Locale("be", "BY"))

        binding.titleDate.text = datePattern.format(calendar.time).replaceFirstChar { it.uppercase() }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) {
                binding.hiddenPlaceholder.visibility = View.VISIBLE
                binding.mainScheduleContent.visibility = View.GONE
                return@observe
            }
            binding.hiddenPlaceholder.visibility = View.GONE
            binding.mainScheduleContent.visibility = View.VISIBLE

            sharedPrefsUseCase.setDefaultScheduleTitle(schedule.getTitle())
            val widgetIntent = Intent(context, ScheduleWidget::class.java)
            val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(requireContext(), ScheduleWidget::class.java))
            widgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            requireContext().sendBroadcast(widgetIntent)
        }

        currentWeekVM.getCurrentWeek()
        currentWeekVM.currentWeekStatus.observe(viewLifecycleOwner) { currentWeek ->
            binding.titleWeekNumber.text = "$currentWeek $weekText"
        }

        binding.settingsButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_settingsFragment)
        }

        binding.scheduleListButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_to_schedules_list)
        }

        currentWeekVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateStatus = StateDialog(errorStatus)
                stateStatus.isCancelable = true
                stateStatus.show(parentFragmentManager, "ErrorDialog")
                currentWeekVM.closeError()
            }
        }

        groupScheduleVM.successStatus.observe(viewLifecycleOwner) { successCode ->
            if (successCode != null) {
                val messageManager = ErrorMessage(requireContext()).get(successCode)
                groupScheduleVM.setSuccessNull()
                Toast.makeText(context, messageManager.title, Toast.LENGTH_SHORT).show()
            }
        }

        groupScheduleVM.scheduleLoadedStatus.observe(viewLifecycleOwner) { savedSchedule ->
            if (savedSchedule == null) return@observe
            if (savedSchedule.isGroup) {
                savedSchedule.group.isSaved = true
                groupItemsVM.saveGroupItem(savedSchedule.group)
            } else {
                savedSchedule.employee.isSaved = true
                employeeItemsVM.saveEmployeeItem(savedSchedule.employee)
            }
            savedItemsVM.saveSchedule(savedSchedule)
            groupScheduleVM.setScheduleLoadedNull()
        }

        return binding.root
    }

}



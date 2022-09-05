package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.data.repository.SharedPrefsRepositoryImpl
import com.bsuir.bsuirschedule.databinding.FragmentSplashScreenBinding
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.viewModels.*
import org.koin.androidx.navigation.koinNavGraphViewModel

class SplashScreenFragment : Fragment() {

    private val initialDataVM: InitialDataViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val currentWeekVM: CurrentWeekViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashScreenBinding.inflate(inflater)
        val prefs = SharedPrefsRepositoryImpl(context!!)

        val isFirstTime = prefs.isFirstTime()

        if (isFirstTime) {
            prefs.setFirstTime(false)
            initialDataVM.initAllData()
            currentWeekVM.getCurrentWeekAPI()
        } else {
            groupScheduleVM.initActiveSchedule()
            findNavController().navigate(R.id.action_to_mainScheduleFragment)
        }

        initialDataVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val statusState = StateDialog(errorStatus)
                statusState.isCancelable = false
                statusState.show(parentFragmentManager, "errorInStart")
            }
        }

        initialDataVM.allDoneStatus.observe(viewLifecycleOwner) { isAllDone ->
            if (isAllDone) {
                groupItemsVM.updateGroupItems()
                employeeItemsVM.updateEmployeeItems()
                findNavController().navigate(R.id.action_to_mainScheduleFragment)
            }
        }

        return binding.root
    }

}



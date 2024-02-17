package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bsuir.bsuirschedule.BuildConfig
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentSplashScreenBinding
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.presentation.dialogs.InfoDialog
import com.bsuir.bsuirschedule.presentation.viewModels.*
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

class SplashScreenFragment : Fragment(), KoinComponent {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val currentWeekVM: CurrentWeekViewModel by koinNavGraphViewModel(R.id.navigation)
    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashScreenBinding.inflate(inflater)

        val isFirstTime = sharedPrefsUseCase.isFirstTime()

        if (isFirstTime) {
            val todayDateFormat = SimpleDateFormat("dd.MM.yyyy")
            val todayDate = todayDateFormat.format(Date().time)
            sharedPrefsUseCase.setScheduleAutoUpdateDate(todayDate)
            sharedPrefsUseCase.setFirstTime(false)
            sharedPrefsUseCase.setScheduleCounter(BuildConfig.SCHEDULES_UPDATE_COUNTER)
            groupItemsVM.updateInitDataAndGroups()
            employeeItemsVM.updateDepartmentsAndEmployeeItems()
            currentWeekVM.getCurrentWeekAPI()
            findNavController().navigate(R.id.action_splashScreenFragment_to_welcomeFragment)
        } else {
//            val prevVersion = sharedPrefsUseCase.getPrevVersion()
//            if (prevVersion < BuildConfig.VERSION_CODE) {
//                val title = getString(R.string.update_info_title)
//                val description = getString(R.string.update_info_description)
//                val infoDialog = InfoDialog(
//                    title = title,
//                    description = description
//                )
//                infoDialog.show(parentFragmentManager, "WarningDialog")
//                sharedPrefsUseCase.setPrevVersion(BuildConfig.VERSION_CODE)
//            }
            groupScheduleVM.initActiveSchedule()
            currentWeekVM.setCurrentWeekNumber()
            findNavController().navigate(R.id.action_to_mainScheduleFragment)
        }

        return binding.root
    }

}



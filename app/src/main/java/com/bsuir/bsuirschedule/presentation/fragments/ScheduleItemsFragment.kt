package com.bsuir.bsuirschedule.presentation.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleItemsBinding
import com.bsuir.bsuirschedule.notification.ScheduleNotification
import com.bsuir.bsuirschedule.presentation.dialogs.PermissionDialog
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.utils.ErrorMessage
import com.bsuir.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleItemsFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleItemsBinding.inflate(inflater)

        groupItemsVM.getAllGroupItems()
        employeeItemsVM.getAllEmployeeItems()
        groupItemsVM.updateIfFirstTime()
        employeeItemsVM.updateIfFirstTime()

        binding.updateButton.setOnClickListener {
            groupItemsVM.updateInitDataAndGroups()
            employeeItemsVM.updateDepartmentsAndEmployeeItems()
            Toast.makeText(requireContext(), "Данные обновляются", Toast.LENGTH_SHORT).show()
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        val notificationPermissionGranted = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    ScheduleNotification.NOTIFICATION_PERMISSION_CODE,
                )
            } else {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_update_channel_id))
                }
                startActivity(intent)
            }
        }

        groupScheduleVM.successStatus.observe(viewLifecycleOwner) { successCode ->
            if (successCode != null) {
                val messageManager = ErrorMessage(context!!).get(successCode)
                groupScheduleVM.setSuccessNull()
                Toast.makeText(context, messageManager.title, Toast.LENGTH_SHORT).show()

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val permissionDialog = PermissionDialog(
                        title = getString(R.string.notification_permission_title),
                        description = getString(R.string.notification_permission_description),
                        permissionGranted = notificationPermissionGranted,
                    )
                    permissionDialog.show(parentFragmentManager, "notificationPermissionDialog")
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

        return binding.root
    }

}



package by.devsgroup.iis.ui.component.drawerSheet

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.devsgroup.iis.R
import by.devsgroup.iis.navController.navigateFinal
import by.devsgroup.iis.navController.navigateUntil
import by.devsgroup.iis.navigation.ScreenNav
import by.devsgroup.schedule.ext.fullName
import by.devsgroup.schedule.ui.model.PreviewScheduleType
import by.devsgroup.schedule.ui.viewModel.PreviewScheduleViewModel
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel
import coil.compose.AsyncImage

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AppModalDrawerSheet(
    navController: NavController,
    onClose: () -> Unit,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val scrollState = rememberScrollState()

    val drawerWidth = remember(screenWidthDp) {
        minOf(screenWidthDp * 0.8f, 400.dp)
    }

    val previewSchedules = previewScheduleViewModel.previewSchedules.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        previewScheduleViewModel.getAllSchedules()
    }

    val schedules = previewSchedules.value ?: listOf()

    val currentScheduleId = scheduleViewModel.currentScheduleId.collectAsStateWithLifecycle()

    ModalDrawerSheet(
        modifier = Modifier
            .width(drawerWidth)
            .fillMaxHeight()
            .verticalScroll(scrollState),
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 32.dp),
            text = "Расписание БГУИР",
            style = MaterialTheme.typography.titleSmall,
        )
        if (schedules.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
            schedules.forEach { schedule ->
                NavigationDrawerItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    selected = currentScheduleId.value == schedule.scheduleId,
                    label = {
                        when(schedule) {
                            is PreviewScheduleType.Employee -> {
                                Text(
                                    text = schedule.fullName(),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            is PreviewScheduleType.Group -> {
                                Text(
                                    text = schedule.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    },
                    icon = {
                        when(schedule) {
                            is PreviewScheduleType.Employee -> {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(32.dp)),
                                    model = schedule.image,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                            is PreviewScheduleType.Group -> {
                                Icon(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(4.dp),
                                    painter = painterResource(R.drawable.ic_group),
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    onClick = {
                        onClose()
                        scheduleViewModel.setCurrentScheduleId(schedule.scheduleId)
                        navController.navigateFinal(ScreenNav.Home.route)
                    }
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        NavigationDrawerItem(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = "Новое расписание",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selected = false,
            onClick = {
                onClose()
                navController.navigateUntil(ScreenNav.AllGroupsAndEmployees.route)
            }
        )
        NavigationDrawerItem(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selected = false,
            onClick = { false }
        )
        Spacer(Modifier.height(32.dp))
    }

}
package by.devsgroup.iis.ui.component.drawerSheet

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun AppModalDrawerSheet() {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val drawerWidth = remember(screenWidthDp) {
        minOf(screenWidthDp * 0.8f, 400.dp)
    }

    val schedules = listOf(
        "151003",
        "325056",
        "Игорь Абрамов Иванович",
        "888554",
        "Виктор Иванов Сергеевич",
    )

    ModalDrawerSheet(
        modifier = Modifier
            .width(drawerWidth)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Расписание БГУИР",
            style = MaterialTheme.typography.titleSmall,
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        schedules.forEach { schedule ->
            NavigationDrawerItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                label = {
                    Text(
                        text = schedule,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                selected = false,
                onClick = { false }
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        NavigationDrawerItem(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            label = {
                Text(
                    text = "Новое расписание",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selected = false,
            onClick = { false }
        )
        NavigationDrawerItem(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            label = {
                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selected = false,
            onClick = { false }
        )
    }

}
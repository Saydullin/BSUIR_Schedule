package by.devsgroup.groups.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.devsgroup.groups.ui.model.GroupUI
import by.devsgroup.ui_kit.item.ListItem

@Composable
fun GroupItem(
    group: GroupUI
) {
    val course = if (group.course != null) "${group.course} курс" else ""

    if (group.name.isNullOrBlank()) return

    ListItem(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        onClick = {

        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = course,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${group.facultyAbbrev} ${group.specialityAbbrev}".trim(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}
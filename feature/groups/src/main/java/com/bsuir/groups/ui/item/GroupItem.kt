package com.bsuir.groups.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bsuir.groups.ui.model.GroupUI

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupItem(
    group: GroupUI,
    downloaded: Boolean,
    shapes: ListItemShapes,
    onClick: () -> Unit,
) {
    val course = if (group.course != null) "${group.course} курс" else ""

    if (group.name.isNullOrBlank()) return

    SegmentedListItem(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        onClick = onClick,
        shapes = shapes,
        overlineContent = if (downloaded) {
            {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = "загружено"
                )
            }
        } else null,
        supportingContent = {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
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
    }

}



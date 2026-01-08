package by.devsgroup.employees.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import by.devsgroup.employees.ui.model.EmployeeUI
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EmployeeItem(
    employeeUI: EmployeeUI,
    downloaded: Boolean,
    shapes: ListItemShapes,
    onClick: () -> Unit
) {

    val departmentsText = employeeUI.departments
        .mapNotNull { it.name }
        .joinToString(", ")
        .trim()

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
                    text = departmentsText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        leadingContent = {
            AsyncImage(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = employeeUI.photoLink,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = employeeUI.getFullName(),
                style = MaterialTheme.typography.titleSmall
            )
            employeeUI.degree?.let { degree ->
                Spacer(Modifier.width(8.dp))
                Text(
                    text = degree,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}
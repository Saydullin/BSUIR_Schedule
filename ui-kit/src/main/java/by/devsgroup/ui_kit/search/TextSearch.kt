package by.devsgroup.ui_kit.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import by.devsgroup.ui_kit.R

@Composable
fun TextSearch(
    modifier: Modifier = Modifier,
    search: String,
    onSearchChange: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(300.dp, 600.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp, 14.dp, 4.dp, 4.dp)),
            value = search,
            onValueChange = { onSearchChange(it) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (search.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onSearchChange("")
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Иконка очистки поле ввода"
                        )
                    }
                }
            },
        )
    }

}



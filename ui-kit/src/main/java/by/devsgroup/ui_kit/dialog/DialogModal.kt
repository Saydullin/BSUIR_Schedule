package by.devsgroup.ui_kit.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DialogModal(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    onSkip: (() -> Unit) = {},
    positiveButtonText: String,
    negativeButtonText: String? = null,
) {
    AlertDialog(
        onDismissRequest = onSkip,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = positiveButtonText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        icon = icon,
        dismissButton = onDismiss?.let {
            {
                negativeButtonText?.let {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = negativeButtonText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}
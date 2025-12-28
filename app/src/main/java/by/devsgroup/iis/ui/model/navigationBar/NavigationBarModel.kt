package by.devsgroup.iis.ui.model.navigationBar

import androidx.compose.ui.graphics.painter.Painter

data class NavigationBarModel(
    val icon: Painter,
    val selectedIcon: Painter,
    val route: String,
    val title: String,
)

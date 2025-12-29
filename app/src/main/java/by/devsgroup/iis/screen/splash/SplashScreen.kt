package by.devsgroup.iis.screen.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import by.devsgroup.iis.navController.navigateFinal
import by.devsgroup.iis.navigation.ScreenNav
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {
        delay(1100)

        navController.navigateFinal(ScreenNav.Home.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Расписания БГУИР",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier
                    .alpha(.4f),
                text = "By Saydullin",
                style = MaterialTheme.typography.titleLarge
            )
        }

    }

}
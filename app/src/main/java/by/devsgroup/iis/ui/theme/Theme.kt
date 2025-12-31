package by.devsgroup.iis.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2F46E0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8E8FF),
    onPrimaryContainer = Color(0xFF07123F),

    secondary = Color(0xFF6F49D1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0D4FC),
    onSecondaryContainer = Color(0xFF2A1650),

    tertiary = Color(0xFFD6C9FF),
    onTertiary = Color(0xFF2B0050),
    tertiaryContainer = Color(0xFFFFF6FF),
    onTertiaryContainer = Color(0xFF321B54),

    background = Color(0xFFF1F3F8),
    onBackground = Color(0xFF111216),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111216),
    surfaceVariant = Color(0xFFE9E6EF),
    onSurfaceVariant = Color(0xFF4B4650),
    inverseSurface = Color(0xFF141418),
    inverseOnSurface = Color(0xFFF6F6F8),
    outline = Color(0xFFBDB9C3),

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    surfaceTint = Color(0xFF2F46E0),

    surfaceBright = Color(0xFFF6F7F9),
    surfaceContainerLow = Color(0xFFF8F9FB),
    surfaceContainer = Color(0xFFF2F3F6),
    surfaceContainerHigh = Color(0xFFEEEFF2),
    surfaceContainerHighest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFDDE1E9),

    primaryFixed = Color(0xFF2F46E0),
    primaryFixedDim = Color(0xFF2539B5),
    onPrimaryFixed = Color.White,
    onPrimaryFixedVariant = Color(0xFFE7E9FF),

    secondaryFixed = Color(0xFF6F49D1),
    secondaryFixedDim = Color(0xFF5636A2),
    onSecondaryFixed = Color.White,
    onSecondaryFixedVariant = Color(0xFFFAF6FF),

    tertiaryFixed = Color(0xFFD6C9FF),
    tertiaryFixedDim = Color(0xFFC1B3FF),
    onTertiaryFixed = Color(0xFF2B0050),
    onTertiaryFixedVariant = Color(0xFFFFF6FF)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4B46E0),
    onPrimary = Color(0xFFFFFFFF),

    primaryContainer = Color(0xFF2A2370),
    onPrimaryContainer = Color(0xFFDAD7FF),

    secondary = Color(0xFF5B2DB3),
    onSecondary = Color(0xFFFFFFFF),

    secondaryContainer = Color(0xFF3A1B6F),
    onSecondaryContainer = Color(0xFFEBDFFF),

    tertiary = Color(0xFF9B8CFF),
    onTertiary = Color(0xFF1F003F),

    tertiaryContainer = Color(0xFFA798D0),
    onTertiaryContainer = Color(0xFFF2EFFF),

    background = Color(0xFF0D0E12),
    onBackground = Color(0xFFEAEAF0),

    surface = Color(0xFF141418),
    onSurface = Color(0xFFECECF2),
    surfaceVariant = Color(0xFF3A3740),
    onSurfaceVariant = Color(0xFFC9C6D1),

    surfaceTint = Color(0xFF4B46E0),

    inverseSurface = Color(0xFFECECF2),
    inverseOnSurface = Color(0xFF111114),

    outline = Color(0xFF9896A3),

    error = Color(0xFFBE6D68),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),

    surfaceBright = Color(0xFF1E1D21),
    surfaceContainerLow = Color(0xFF232126),
    surfaceContainer = Color(0xFF15151A),
    surfaceContainerHigh = Color(0xFF19181C),
    surfaceContainerHighest = Color(0xFF27272C),
    surfaceDim = Color(0xFF0C0C0F),

    primaryFixed = Color(0xFF4B46E0),
    primaryFixedDim = Color(0xFF2F2A84),
    onPrimaryFixed = Color(0xFFFFFFFF),
    onPrimaryFixedVariant = Color(0xFFDAD7FF),

    secondaryFixed = Color(0xFF5B2DB3),
    secondaryFixedDim = Color(0xFF3D1B85),
    onSecondaryFixed = Color(0xFFFFFFFF),
    onSecondaryFixedVariant = Color(0xFFEBDFFF),

    tertiaryFixed = Color(0xFF9B8CFF),
    tertiaryFixedDim = Color(0xFF7A66D9),
    onTertiaryFixed = Color(0xFF1F003F),
    onTertiaryFixedVariant = Color(0xFFF2EFFF)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val shapes = MaterialTheme.shapes.copy(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(6.dp),
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}



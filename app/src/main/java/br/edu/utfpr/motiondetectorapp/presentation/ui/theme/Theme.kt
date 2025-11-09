package br.edu.utfpr.motiondetectorapp.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Colors
import androidx.compose.ui.graphics.Color

private val wearColorPalette = Colors(
    primary = Color(0xFF1E88E5),
    secondary = Color(0xFF00E676),
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun WearMotionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = wearColorPalette,
        content = content
    )
}

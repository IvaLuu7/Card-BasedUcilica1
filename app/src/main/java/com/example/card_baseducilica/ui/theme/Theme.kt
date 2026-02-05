package com.example.card_baseducilica.ui.theme

/*import androidx.compose.foundation.isSystemInDarkTheme*/
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryPurple,
    secondary = PastelPink,
    tertiary = PastelBlue,

    background = BackgroundLight,
    surface = SurfaceLight,

    onPrimary = Color.White,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,

    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun CardBasedUcilicaTheme(
   /* darkTheme: Boolean = isSystemInDarkTheme(),*/
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
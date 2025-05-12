package com.pnj.saku_planner.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppColor.Primary,
    onPrimary = AppColor.PrimaryForeground,

    secondary = AppColor.Secondary,
    onSecondary = AppColor.SecondaryForeground,

    background = AppColor.Background,
    onBackground = AppColor.Foreground,

    surface = AppColor.Background,
    onSurface = AppColor.Foreground,

    error = AppColor.Destructive,
    onError = AppColor.DestructiveForeground,

    onPrimaryContainer = AppColor.CardForeground,
    primaryContainer = AppColor.Card,

    onSecondaryContainer = AppColor.CardForeground,
    secondaryContainer = AppColor.Card,

    onTertiaryContainer = AppColor.CardForeground,
    tertiaryContainer = AppColor.Card,

    onSurfaceVariant = AppColor.MutedForeground,

    outline = AppColor.Border
)


@Composable
fun SakuPlannerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
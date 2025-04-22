package com.pnj.saku_planner.ui.theme

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
    surface = AppColor.Card,
    onSurface = AppColor.CardForeground,
    error = AppColor.Destructive,
    onError = AppColor.DestructiveForeground,
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
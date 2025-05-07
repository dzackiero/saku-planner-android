package com.pnj.saku_planner.home.domain.enum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.pnj.saku_planner.ui.theme.AppColor

enum class KakeiboCategory {
    NEEDS,
    WANTS,
    CULTURE,
    UNEXPECTED;

    fun getStyle(): KakiboCategoryStyle {
        return when (this) {
            NEEDS -> KakiboCategoryStyle(
                icon = Icons.Outlined.Home,
                text = "Needs",
                subtext = "Essentials",
                color = AppColor.Needs,
            )

            WANTS -> KakiboCategoryStyle(
                icon = Icons.Outlined.FavoriteBorder,
                text = "Wants",
                subtext = "Non-essentials",
                color = AppColor.Wants
            )

            CULTURE -> KakiboCategoryStyle(
                icon = Icons.Outlined.ThumbUp,
                text = "Culture",
                subtext = "Enrichment",
                color = AppColor.Culture
            )

            UNEXPECTED -> KakiboCategoryStyle(
                icon = Icons.Outlined.Info,
                text = "Unexpected",
                subtext = "Emergencies",
                color = AppColor.Unexpected
            )
        }
    }
}

data class KakiboCategoryStyle(
    val icon: ImageVector,
    val text: String,
    val subtext: String,
    val color: Color,
)

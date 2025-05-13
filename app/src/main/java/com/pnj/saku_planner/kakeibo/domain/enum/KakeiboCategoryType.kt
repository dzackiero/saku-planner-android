package com.pnj.saku_planner.kakeibo.domain.enum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.pnj.saku_planner.core.ui.theme.AppColor

enum class KakeiboCategoryType {
    NEEDS,
    WANTS,
    CULTURE,
    UNEXPECTED;

    fun getStyle(): KakeiboCategoryStyle {
        return when (this) {
            NEEDS -> KakeiboCategoryStyle(
                icon = Icons.Outlined.Home,
                text = "Needs",
                subtext = "Essentials",
                color = AppColor.Needs,
            )

            WANTS -> KakeiboCategoryStyle(
                icon = Icons.Outlined.FavoriteBorder,
                text = "Wants",
                subtext = "Non-essentials",
                color = AppColor.Wants
            )

            CULTURE -> KakeiboCategoryStyle(
                icon = Icons.Outlined.ThumbUp,
                text = "Culture",
                subtext = "Enrichment",
                color = AppColor.Culture
            )

            UNEXPECTED -> KakeiboCategoryStyle(
                icon = Icons.Outlined.Info,
                text = "Unexpected",
                subtext = "Emergencies",
                color = AppColor.Unexpected
            )
        }
    }
}

data class KakeiboCategoryStyle(
    val icon: ImageVector,
    val text: String,
    val subtext: String,
    val color: Color,
)



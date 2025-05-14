package com.pnj.saku_planner.kakeibo.presentation.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.kakeibo.presentation.components.TargetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography

@Composable
fun TargetScreen() {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Completed", "Pending")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Saving Targets",
                style = Typography.displaySmall
            )

            PrimaryButton(onClick = {}) {
                Text(
                    text = "Add Target",
                    style = Typography.titleMedium,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppColor.AccentForeground,
                        selectedLabelColor = AppColor.Accent,
                    ),
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(filter)
                    },
                )
            }
        }

        Column {
            TargetCard(
                title = "New Card",
                account = "Test",
                amount = 100_000,
                totalAmount = 1_000_000,
                progress = 0.2f,
                monthlyAmount = 200000
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TargetScreenPreview() {
    SakuPlannerTheme {
        TargetScreen()
    }
}


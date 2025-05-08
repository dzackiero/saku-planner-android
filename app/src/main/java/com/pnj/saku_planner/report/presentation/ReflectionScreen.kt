package com.pnj.saku_planner.report.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.ui.components.Card
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.ui.theme.Typography

@Composable
fun ReflectionScreen(modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column {
                    Text(
                        text = "Kakeibo Reflection Questions"
                    )
                    Text(
                        text = "Answer these questions to improve your financial habits.",
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall,
                    )
                }
                Column {
                    Text(
                        text = "1. How much money do you have?",
                        style = Typography.titleMedium,
                    )
                    Text(
                        text = "Total accross all accounts: $90.00",
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall,
                    )
                }
                Column {
                    Text(
                        text = "1. How much would you like to save?",
                        style = Typography.titleMedium,
                    )
                    Text(
                        text = "Current monthly savings: \$90.00 (90.0% of income)",
                        color = AppColor.MutedForeground,
                        style = Typography.labelSmall,
                    )
                }
                Box {
                    Text(
                        text = "Wallet",
                        style = Typography.titleMedium,
                        modifier = Modifier
                            .clickable { isExpanded = true }
                            .padding(16.dp)

                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReflectionScreenPreview() {
    SakuPlannerTheme {
        ReflectionScreen()
    }
}


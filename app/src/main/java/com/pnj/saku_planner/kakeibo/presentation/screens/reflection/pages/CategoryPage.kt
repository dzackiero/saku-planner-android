package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ChartData
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PieChartWithText
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency

@Composable
fun SpendingCategoryPage() {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.size(1.dp))
        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {

                Text(
                    text = "Where Did April's Money Go?",
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Your spending categories this month",
                    style = Typography.titleMedium,
                    color = AppColor.MutedForeground
                )
            }
            PieChartWithText(
                chartDataList = listOf(
                    ChartData("Food", AppColor.Primary, 300_000),
                    ChartData("Transport", AppColor.Secondary, 200_000),
                    ChartData("Entertainment", AppColor.Success, 150_000),
                    ChartData("Utilities", AppColor.Destructive, 100_000),
                ),
                totalFormatter = { formatToCurrency(it) },
                startupAnimation = false,
                totalLabel = "Total",
            )
            Text(
                text = "Top Spending",
                style = Typography.titleMedium,
            )
            Card(padding = PaddingValues(horizontal = 4.dp)) {
                Row {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "50%",
                                style = Typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier
                                    .background(AppColor.Success, shape = RoundedCornerShape(2.dp))
                                    .padding(4.dp)
                            )

                            Text(
                                text = "Entertainment",
                                style = Typography.bodyMedium,
                            )
                        }
                        Text(
                            text = formatToCurrency(50_000),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
            Text(
                text = "Least Spending",
                style = Typography.titleMedium,
            )
            Card(padding = PaddingValues(horizontal = 4.dp)) {
                Row {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "50%",
                                style = Typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier
                                    .background(AppColor.Success, shape = RoundedCornerShape(2.dp))
                                    .padding(4.dp)
                            )

                            Text(
                                text = "Entertainment",
                                style = Typography.bodyMedium,
                            )
                        }
                        Text(
                            text = formatToCurrency(50_000),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Button Navigation
        Row(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SecondaryButton(
                onClick = {},
                modifier = Modifier
                    .width(120.dp)
            ) {
                Text("Previous")
            }
            PrimaryButton(
                onClick = {},
                modifier = Modifier.width(120.dp)
            ) {
                Text("Next")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendingCategoryPagePreview() {
    KakeiboTheme {
        SpendingCategoryPage()
    }
}
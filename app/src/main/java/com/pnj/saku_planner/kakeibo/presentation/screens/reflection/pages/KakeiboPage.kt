package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ChartData
import com.pnj.saku_planner.kakeibo.presentation.components.ui.ClickableTextField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PieChartWithText
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency

@Composable
fun KakeiboReflectionPage() {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.size(1.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Kakeibo Reflection",
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "How your expenses aligned with the Kakeibo Method",
                    style = Typography.titleMedium,
                    color = AppColor.MutedForeground,
                    textAlign = TextAlign.Center,
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    PieChartWithText(
                        chartDataList = listOf(
                            ChartData("Needs", AppColor.Needs, 250_000),
                            ChartData("Wants", AppColor.Wants, 150_000),
                            ChartData("Culture", AppColor.Culture, 100_000),
                            ChartData("Unexpected", AppColor.Unexpected, 50_000)
                        ),
                        totalLabel = "Total",
                        totalFormatter = { formatToCurrency(it) },
                        startupAnimation = false
                    )
                }
            }


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Which purchase made you happiest?",
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(bottom = 2.dp),
                    textAlign = TextAlign.Center,
                )
                ClickableTextField(
                    value = "",
                    label = { Text("My favorite purchase is...") },
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Any spending regrets?",
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(bottom = 2.dp),
                    textAlign = TextAlign.Center,
                )
                ClickableTextField(
                    value = "",
                    label = { Text("or maybe you don't have any!") },
                )
            }
        }


//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text(
//                    text = "Kakeibo Reflection",
//                    style = Typography.displayMedium,
//                    fontWeight = FontWeight.Bold,
//                )
//                Text(
//                    text = "How your expenses aligned with the Kakeibo Method",
//                    style = Typography.titleMedium,
//                    color = AppColor.MutedForeground,
//                    textAlign = TextAlign.Center,
//                )
//            }
//            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                Card(modifier = Modifier.fillMaxWidth()) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Text(
//                            text = "Needs",
//                            style = Typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold,
//                            color = AppColor.Needs
//                        )
//                        Text(
//                            text = "Rp 1,500,000",
//                            style = Typography.headlineMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = AppColor.Needs,
//                            overflow = TextOverflow.Ellipsis,
//                            maxLines = 1,
//                        )
//                        Text(
//                            text = "100.0%",
//                            style = Typography.bodyMedium,
//                            color = AppColor.Needs
//                        )
//                    }
//                }
//                Card(modifier = Modifier.fillMaxWidth()) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Text(
//                            text = "Wants",
//                            style = Typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold,
//                            color = AppColor.Wants
//                        )
//                        Text(
//                            text = "Rp 1,500,000",
//                            style = Typography.headlineMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = AppColor.Wants,
//                        )
//                        Text(
//                            text = "100.0%",
//                            style = Typography.bodyMedium,
//                            color = AppColor.Wants
//                        )
//                    }
//                }
//                Card(modifier = Modifier.fillMaxWidth()) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Text(
//                            text = "Culture",
//                            style = Typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold,
//                            color = AppColor.Culture
//                        )
//                        Text(
//                            text = "Rp 1,500,000",
//                            style = Typography.headlineMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = AppColor.Culture,
//                            overflow = TextOverflow.Ellipsis,
//                            maxLines = 1,
//                        )
//                        Text(
//                            text = "100.0%",
//                            style = Typography.bodyMedium,
//                            color = AppColor.Culture
//                        )
//                    }
//                }
//                Card(modifier = Modifier.fillMaxWidth()) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Text(
//                            text = "Unexpected",
//                            style = Typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold,
//                            color = AppColor.Unexpected
//                        )
//                        Text(
//                            text = "Rp 1,500,000",
//                            style = Typography.headlineMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = AppColor.Unexpected,
//                        )
//                        Text(
//                            text = "100.0%",
//                            style = Typography.bodyMedium,
//                            color = AppColor.Unexpected
//                        )
//                    }
//                }
//            }
//        }

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
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
fun KakeiboReflectionPagePreview() {
    KakeiboTheme {
        KakeiboReflectionPage()
    }
}

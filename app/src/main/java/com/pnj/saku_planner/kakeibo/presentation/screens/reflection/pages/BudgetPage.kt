package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn // Added for max height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.database.entity.BudgetUi
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.TailwindColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.BudgetCard
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency

@Composable
fun BudgetPage() {
    var showReflectionForm by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.size(1.dp)) // Minimal spacer for top alignment with SpaceBetween

        Column(
            modifier = Modifier.fillMaxWidth(), // This column groups title, overall card, conditional section, and button
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Page Title and Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "How your Budgeting Went",
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Here's a summary of your budgeting for this month.",
                    style = Typography.titleMedium,
                    color = AppColor.MutedForeground,
                    textAlign = TextAlign.Center,
                )
            }

            // Overall Budgets Summary Card (Always Visible)
            Card {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Overall Budgets",
                            style = Typography.titleMedium,
                        )
                    }
                    Text(
                        text = "Rp500.000", // Example Data
                        style = Typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { 0.5f }, // Example Data
                            modifier = Modifier
                                .height(6.dp)
                                .fillMaxWidth(),
                            trackColor = AppColor.Muted,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Rp500.000/Rp1.000.000", // Example Data
                                style = Typography.labelSmall,
                                color = AppColor.MutedForeground,
                            )
                            Text(
                                text = "50%", // Example Data
                                style = Typography.labelSmall,
                                color = AppColor.MutedForeground,
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showReflectionForm,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Space between text and the list
                ) {
                    Text(
                        text = "You spent more than planned in these categories",
                        textAlign = TextAlign.Center,
                        style = Typography.titleMedium,
                        color = AppColor.MutedForeground,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        repeat(30) {
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
                                                text = "120%",
                                                style = Typography.bodySmall,
                                                color = Color.White,
                                                modifier = Modifier
                                                    .background(
                                                        TailwindColor.Yellow500,
                                                        shape = RoundedCornerShape(2.dp)
                                                    )
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
                    }
                }
            }

            // Toggle Button
            SecondaryButton(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                onClick = {
                    showReflectionForm = !showReflectionForm
                }
            ) {
                Text(
                    text = if (showReflectionForm) "View Budget Details" else "Hide Budget Details",
                    style = Typography.titleMedium,
                    color = AppColor.Foreground,
                )
            }
        }

        AnimatedVisibility(
            visible = !showReflectionForm,
            modifier = Modifier.weight(1f),
            enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> -fullHeight / 2 }) + expandVertically(
                expandFrom = Alignment.Top
            ),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight / 2 }) + shrinkVertically(
                shrinkTowards = Alignment.Top
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(20) {
                    BudgetCard(
                        budget = BudgetUi(
                            id = "lorem-ipsum",
                            category = "Entertainment",
                            amount = 100_000,
                            currentAmount = 500_000,
                        )
                    ) { }
                }
            }
        }


        // Bottom Navigation Buttons
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
fun BudgetPagePreview() {
    KakeiboTheme {
        BudgetPage()
    }
}


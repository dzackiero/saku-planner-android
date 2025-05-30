package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton

@Composable
fun SavingPage() {
    var showReflectionForm by remember { mutableStateOf(true) }
    var reflectionText by remember { mutableStateOf("") }
    var selectedFeeling by remember { mutableStateOf<String?>(null) }


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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "Your April 2025 Savings",
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Here's how much you saved this month according to your plans",
                    style = Typography.titleMedium,
                    color = AppColor.MutedForeground,
                    textAlign = TextAlign.Center,
                )
            }

            // Overall Savings Summary Card (Always Visible)
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
                            text = "All Savings",
                            style = Typography.titleMedium,
                        )
                    }
                    Text(
                        text = "Rp500.000",
                        style = Typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { 0.5f },
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
                                "Rp500.000/Rp1.000.000",
                                style = Typography.labelSmall,
                                color = AppColor.MutedForeground,
                            )
                            Text(
                                text = "50%",
                                style = Typography.labelSmall,
                                color = AppColor.MutedForeground,
                            )
                        }
                    }
                }
            }

            // Reflection Form Section (Visible when showReflectionForm is true)
            AnimatedVisibility(
                visible = showReflectionForm,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // "How Do You Feel?" Section
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "How Do You Feel About Your Savings?",
                            style = Typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            val feelings = listOf("Great" to "😁", "Okay" to "😐", "Not Good" to "😔")
                            feelings.forEach { (text, emoji) ->
                                InputChip(
                                    modifier = Modifier.weight(1f),
                                    selected = selectedFeeling == text,
                                    label = {
                                        Column(
                                            modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Text(emoji, style = Typography.headlineSmall)
                                            Text(
                                                text = text,
                                                style = Typography.bodySmall,
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedFeeling =
                                            if (selectedFeeling == text) null else text
                                    },
                                )
                            }
                        }
                    }

                    // "What helped or hurt?" Section
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "What helped or hurt your savings most?",
                            style = Typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                        )
                        OutlinedTextField(
                            value = reflectionText,
                            onValueChange = { reflectionText = it },
                            label = { Text("Share your thoughts") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                        )
                    }
                }
            }

            SecondaryButton(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp), // Spacing for the button
                onClick = {
                    showReflectionForm = !showReflectionForm
                }
            ) {
                Text(
                    text = if (showReflectionForm) "View Savings Details" else "Reflect on Savings",
                    style = Typography.titleMedium,
                    color = AppColor.Foreground,
                )
            }
        }

        // Savings Details Section (Visible when showReflectionForm is false)
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
                repeat(4) {
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
                                    text = "Savings Plan ${it + 1}",
                                    style = Typography.titleMedium,
                                )
                            }
                            Text(
                                text = "Rp${(it + 1) * 100}.000",
                                style = Typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { ((it + 1) * 0.2f).coerceAtMost(1f) },
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
                                        "Rp${(it + 1) * 100}.000/Rp500.000",
                                        style = Typography.labelSmall,
                                        color = AppColor.MutedForeground,
                                    )
                                    Text(
                                        text = "${((it + 1) * 20)}%",
                                        style = Typography.labelSmall,
                                        color = AppColor.MutedForeground,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


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
fun SavingPagePreview() {
    KakeiboTheme {
        SavingPage()
    }
}

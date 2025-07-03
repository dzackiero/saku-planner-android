package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.components.ui.yearMonthToString
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import timber.log.Timber
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SavingPage(
    state: ReflectionState = ReflectionState(),
    callbacks: ReflectionCallbacks = ReflectionCallbacks(),
    navigateToSavingDetail: (String) -> Unit = {},
) {
    var showReflectionForm by remember { mutableStateOf(true) }
    val accountNames = state.savings.map { it.name }
    val transactions = state.transactions.filter {
        accountNames.contains(it.account)
    }
    val fullTarget = state.savings.sumOf {
        (it.target?.targetAmount ?: 0) / (it.target?.duration ?: 1)
    }

    val fullPercentage = if (fullTarget > 0) {
        transactions.sumOf { it.amount } / fullTarget.toDouble()
    } else {
        0.0
    }
    val fullPercentageString = String.format(
        Locale.getDefault(),
        "%.2f%%",
        fullPercentage * 100
    )

    val savingsFeedback = if (fullPercentage >= 1) {
        "Congratulations! You've hit your savings goal for this month!"
    } else {
        "You're on your way to hitting your savings goal. Keep it up!"
    }

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
                text = stringResource(
                    R.string.saving_title,
                    yearMonthToString(state.yearMonth, TextStyle.FULL)
                ),
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.saving_desc),
                style = Typography.titleMedium,
                color = AppColor.MutedForeground,
                textAlign = TextAlign.Center,
            )
        }

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
                        text = stringResource(R.string.all_savings),
                        style = Typography.titleMedium,
                    )
                }
                Text(
                    text = formatToCurrency(fullTarget),
                    style = Typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { fullPercentage.toFloat() },
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
                            text = stringResource(
                                R.string.per_month,
                                formatToCurrency(fullTarget)
                            ),
                            style = Typography.labelSmall,
                            color = AppColor.MutedForeground,
                        )
                        Text(
                            text = fullPercentageString,
                            style = Typography.labelSmall,
                            color = AppColor.MutedForeground,
                        )
                    }
                }
            }
        }

        Text(
            text = savingsFeedback,
            style = Typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.how_do_you_feel_about_your_savings),
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
                        val feelings = listOf(
                            stringResource(R.string.great) to "😁",
                            stringResource(R.string.okay) to "😐",
                            stringResource(R.string.not_good) to "😔"
                        )
                        feelings.forEach { (text, emoji) ->
                            InputChip(
                                modifier = Modifier.weight(1f),
                                selected = state.savingFeeling == text,
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
                                    callbacks.onSavingFeelingChanged(text)
                                },
                            )
                        }
                    }
                }

                // "What helped or hurt?" Section
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.what_helped_or_hurt_your_savings_most),
                        style = Typography.titleMedium,
                        modifier = Modifier.padding(bottom = 4.dp),
                        textAlign = TextAlign.Center,
                    )
                    OutlinedTextField(
                        value = state.savingNote ?: "",
                        onValueChange = callbacks.onSavingNoteChanged,
                        label = { Text(stringResource(R.string.share_your_thoughts)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                    )
                }
            }
        }

        SecondaryButton(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
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
                state.savings.forEach { saving ->
                    val amount = transactions
                        .filter { it.account == saving.name }.sumOf { it.amount }
                    val monthlyTarget =
                        (saving.target?.targetAmount ?: 0) /
                                (saving.target?.duration?.toDouble() ?: 1.0)

                    val actualProgress = if (monthlyTarget > 0) {
                        amount.toDouble() / monthlyTarget
                    } else {
                        if (amount > 0) 1.0
                        else 0.0
                    }

                    val percentageString =
                        String.format(Locale.getDefault(), "%.2f%%", actualProgress * 100)

                    val isOverBudget = actualProgress > 1.0

                    val progressIndicatorColor =
                        if (isOverBudget) AppColor.Destructive else AppColor.Primary

                    val percentageTextColor =
                        if (isOverBudget) AppColor.Destructive else Color.Unspecified

                    Timber.d("Saving: ${saving.name}, Amount: $amount, Monthly Target: $monthlyTarget, Actual Progress: $actualProgress")

                    Card(
                        modifier = Modifier.clickable { navigateToSavingDetail(saving.id) }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = saving.name,
                                    style = Typography.titleMedium,
                                )
                            }
                            Text(
                                text = formatToCurrency(amount),
                                style = Typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { actualProgress.toFloat() },
                                    color = progressIndicatorColor,
                                    trackColor = AppColor.Muted,
                                    modifier = Modifier
                                        .height(6.dp)
                                        .fillMaxWidth(),
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.per_month,
                                            formatToCurrency(monthlyTarget)
                                        ),
                                        style = Typography.labelSmall,
                                        color = AppColor.MutedForeground,
                                    )
                                    Text(
                                        text = percentageString,
                                        color = percentageTextColor,
                                        style = Typography.labelSmall,
                                    )
                                }
                            }
                        }
                    }
                }
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
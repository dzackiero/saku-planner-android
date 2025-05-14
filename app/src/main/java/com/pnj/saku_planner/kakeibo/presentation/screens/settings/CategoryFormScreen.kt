package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.EmojiPicker
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryFormCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels.CategoryFormState

@Composable
fun CategoryFormScreen(
    modifier: Modifier = Modifier,
    state: CategoryFormState = CategoryFormState(),
    callbacks: CategoryFormCallbacks = CategoryFormCallbacks(),
    onNavigateBack: () -> Unit = {},
) {
    DefaultForm(
        title = "Create Category",
        onNavigateBack = onNavigateBack,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    EmojiPicker(
                        emoji = state.categoryIcon,
                    ) {
                        callbacks.onIconChange(it)
                    }
                    Text(
                        text = stringResource(R.string.category_icon),
                        style = Typography.labelMedium,
                        color = AppColor.MutedForeground
                    )
                }

                OutlinedTextField(
                    value = state.categoryName,
                    placeholder = {
                        Text(stringResource(R.string.category_name))
                    },
                    onValueChange = callbacks.onNameChange,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    InputChip(
                        modifier = Modifier.weight(1f),
                        selected = state.categoryType == TransactionType.EXPENSE,
                        onClick = {
                            callbacks.onTypeChange(TransactionType.EXPENSE)
                        },
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                    contentDescription = stringResource(R.string.expense),
                                    tint = AppColor.Destructive
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = stringResource(R.string.expense),
                                    style = Typography.bodyMedium,
                                )
                            }
                        }
                    )
                    InputChip(
                        modifier = Modifier.weight(1f),
                        selected = state.categoryType == TransactionType.INCOME,
                        onClick = {
                            callbacks.onTypeChange(TransactionType.INCOME)
                        },
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = stringResource(R.string.income),
                                    tint = AppColor.Success
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = stringResource(R.string.income),
                                    style = Typography.bodyMedium,
                                )
                            }
                        }
                    )
                }
            }

            PrimaryButton(
                onClick = {
                    callbacks.onSubmit()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

@Preview
@Composable
fun CategoryFormPreview() {
    SakuPlannerTheme {
        CategoryFormScreen()
    }
}
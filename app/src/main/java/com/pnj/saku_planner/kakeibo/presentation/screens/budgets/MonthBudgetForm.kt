package com.pnj.saku_planner.kakeibo.presentation.screens.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BalanceTextField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.BottomSheetField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Confirmable
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.MonthBudgetFormCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.budgets.viewmodels.MonthBudgetFormState

@Composable
fun MonthBudgetFormScreen(
    title: String = stringResource(R.string.edit_budget),
    state: MonthBudgetFormState = MonthBudgetFormState(),
    callbacks: MonthBudgetFormCallbacks = MonthBudgetFormCallbacks(),
    onSubmit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    DefaultForm(
        title = title,
        onNavigateBack = onNavigateBack,
        actions = {
            if (state.id != null) {
                Confirmable(onConfirmed = onDelete) {
                    IconButton(onClick = it) {
                        Icon(
                            Icons.Outlined.Delete, stringResource(R.string.delete_budget)
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                BottomSheetField(
                    selectedItem = state.selectedCategory,
                    options = listOf(state.selectedCategory),
                    enabled = false,
                    label = {
                        Text(stringResource(R.string.select_category))
                    },
                    onItemSelected = {},
                    itemContent = {
                        Text(
                            text = it?.name ?: "",
                            style = Typography.bodyMedium,
                        )
                    },
                    itemLabel = { it?.name ?: "" },
                )

                BalanceTextField(
                    label = stringResource(R.string.budget_amount),
                    value = state.amount,
                    onValueChange = callbacks.onAmountChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            PrimaryButton(
                onClick = {
                    onSubmit()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.submit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthBudgetFormScreenPreview() {
    SakuPlannerTheme {
        MonthBudgetFormScreen()
    }
}
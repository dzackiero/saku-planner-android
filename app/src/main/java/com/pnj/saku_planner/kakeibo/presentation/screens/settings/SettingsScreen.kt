package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DestructiveButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography

@Composable
fun SettingsScreen(
    navigateToCategories: () -> Unit = {},
    onResetAppData: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = Typography.displayMedium,
        )

        Card {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.manage_categories),
                    style = Typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.manage_your_income_and_expense_categories),
                    color = AppColor.MutedForeground,
                    style = Typography.labelMedium
                )
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = navigateToCategories
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.edit_categories),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = stringResource(R.string.manage_categories))
                }
            }
        }
        Card {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_settings_title),
                    style = Typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.app_settings_subtitle),
                    color = AppColor.MutedForeground,
                    style = Typography.labelMedium
                )
                DestructiveButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onResetAppData
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "delete app data",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.reset_app_data))
                }
                Text(
                    text = stringResource(R.string.app_settings_description),
                    color = AppColor.MutedForeground,
                    style = Typography.labelSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    KakeiboTheme {
        SettingsScreen()
    }
}
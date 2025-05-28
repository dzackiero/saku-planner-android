package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Confirmable

@Composable
fun SettingsScreen(
    workInfo: WorkInfo? = null,
    navigateToCategories: () -> Unit = {},
    navigateToSchedule: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    onManualSyncing: () -> Unit = {},
    onResetAppData: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = Typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
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
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.data_management),
                style = Typography.headlineMedium,
                color = AppColor.MutedForeground,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SettingCard(
                    title = stringResource(R.string.manual_sync),
                    description = stringResource(R.string.manual_sync_desc),
                    icon = Icons.Outlined.CloudUpload,
                    onClick = onManualSyncing
                )
                when (workInfo?.state) {
                    WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                    }

                    else -> {}
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.account_settings),
                style = Typography.headlineMedium,
                color = AppColor.MutedForeground,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SettingCard(
                    title = stringResource(R.string.profile),
                    description = stringResource(R.string.profile_desc),
                    icon = Icons.Outlined.AccountCircle,
                    onClick = navigateToProfile
                )
                SettingCard(
                    title = stringResource(R.string.schedule),
                    description = stringResource(R.string.notification_desc),
                    icon = Icons.Outlined.Settings,
                    onClick = navigateToSchedule
                )
                Confirmable(onConfirmed = onLogout) {
                    SettingCard(
                        title = stringResource(R.string.sign_out),
                        description = stringResource(R.string.sign_out_of_your_account),
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        onClick = it
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.danger_zone),
                style = Typography.headlineMedium,
                color = AppColor.Destructive,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Confirmable(onConfirmed = onResetAppData) {
                    SettingCard(
                        title = stringResource(R.string.reset_app_data),
                        description = stringResource(R.string.reset_app_data_description),
                        icon = Icons.Outlined.Delete,
                        color = AppColor.Destructive,
                        onClick = it
                    )
                }
            }
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: ImageVector? = null,
    color: Color = AppColor.Foreground,
) {
    Card(modifier = modifier.clickable { onClick() }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "icon",
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    color = color,
                    style = Typography.titleMedium
                )
                Text(
                    text = description,
                    color = AppColor.MutedForeground,
                    style = Typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
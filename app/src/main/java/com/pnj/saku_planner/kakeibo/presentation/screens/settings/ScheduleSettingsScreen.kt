package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.data.local.UserSettings
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm

@Composable
fun ScheduleSettingsScreen(
    onNavigateBack: () -> Unit = {},
) {
    val viewModel = hiltViewModel<ScheduleSettingsViewModel>()
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsStateWithLifecycle(UserSettings())


    val settings = listOf(
        SettingItem(
            title = stringResource(R.string.daily_sync),
            description = stringResource(R.string.daily_sync_desc),
            onClick = { },
            action = {
                var isDailySyncEnabled by remember { mutableStateOf(true) }
                Switch(isDailySyncEnabled, onCheckedChange = { isDailySyncEnabled = it })
            }
        ),
        SettingItem(
            title = stringResource(R.string.monthly_reflection_notification),
            description = stringResource(R.string.month_reflection_desc),
            onClick = { },
            action = {
                Switch(
                    checked = state.isMonthlyReflectNotification,
                    onCheckedChange = { viewModel.setReflectionNotification(it) },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        ),
        SettingItem(
            title = stringResource(R.string.monthly_reflection_date),
            description = stringResource(
                R.string.reminder_on_day_of_each_month,
                state.monthReflectionDate
            ),
            onClick = { if (state.isMonthlyReflectNotification) showDatePickerDialog = true },
            action = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.width(24.dp)) {
                        Text(
                            text = state.monthReflectionDate.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = if (state.isMonthlyReflectNotification) MaterialTheme.colorScheme.onSurface else AppColor.MutedForeground
                        )
                    }
                }
            },
            isEnabled = state.isMonthlyReflectNotification
        ),
    )

    DefaultForm(
        title = stringResource(R.string.schedule),
        onNavigateBack = onNavigateBack
    ) {
        Column {
            settings.forEachIndexed { index, item ->
                SettingListItem(
                    title = item.title,
                    description = item.description,
                    onClick = item.onClick,
                    action = item.action,
                    isEnabled = item.isEnabled
                )
                if (index < settings.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }

    if (showDatePickerDialog) {
        ReminderDatePickerDialog(
            currentDate = state.monthReflectionDate,
            onDateSelected = {
                viewModel.setReflectionDate(it)
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
}

@Composable
fun SettingListItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    action: @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true,
) {
    val contentAlpha = if (isEnabled) 1f else 0.5f
    val titleColor = MaterialTheme.colorScheme.onSurface
    val descriptionColor = AppColor.MutedForeground

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .alpha(contentAlpha)
        ) {
            Text(
                text = title,
                style = Typography.titleMedium,
                color = titleColor
            )
            Text(
                text = description,
                color = descriptionColor,
                style = Typography.labelMedium,
            )
        }
        action?.let {
            Box(modifier = Modifier.alpha(contentAlpha)) {
                action()
            }
        }
    }
}

@Composable
fun ReminderDatePickerDialog(
    currentDate: Int,
    onDateSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val dates = (1..28).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.select_date),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(dates) { date ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.small)
                            .clickable { onDateSelected(date) }
                            .padding(4.dp)
                            .then(
                                if (date == currentDate) Modifier.background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.shapes.small
                                ) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.toString(),
                            style = Typography.bodyLarge,
                            color = if (date == currentDate) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}


data class SettingItem(
    val title: String,
    val description: String,
    val onClick: () -> Unit = {},
    val action: @Composable () -> Unit = {},
    val isEnabled: Boolean = true
)

@Preview(showBackground = true)
@Composable
fun ScheduleSettingsScreenPreview() {
    KakeiboTheme {
        ScheduleSettingsScreen()
    }
}
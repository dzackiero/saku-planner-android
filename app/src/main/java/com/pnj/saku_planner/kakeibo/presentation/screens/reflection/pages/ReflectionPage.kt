package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionCallbacks
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ReflectionPage(
    state: ReflectionState = ReflectionState(),
    callbacks: ReflectionCallbacks = ReflectionCallbacks()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(R.string.reflection_planning_title),
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.reflection_planning_desc),
                style = Typography.titleMedium,
                color = AppColor.MutedForeground,
                textAlign = TextAlign.Center,
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(
                    R.string.current_month_reflect_question,
                    state.yearMonth.month.getDisplayName(
                        TextStyle.FULL, Locale.getDefault()
                    )
                ),
                style = Typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center,
            )
            OutlinedTextField(
                value = state.currentMonthNote ?: "",
                onValueChange = callbacks.onCurrentMonthNoteChanged,
                label = { Text(stringResource(R.string.example_curr_month)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.next_month_reflect_question),
                style = Typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            OutlinedTextField(
                value = state.nextMonthNote ?: "",
                onValueChange = callbacks.onNextMonthNoteChanged,
                label = { Text(stringResource(R.string.ex_next_month_question)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReflectionPagePreview() {
    KakeiboTheme {
        ReflectionPage()
    }
}

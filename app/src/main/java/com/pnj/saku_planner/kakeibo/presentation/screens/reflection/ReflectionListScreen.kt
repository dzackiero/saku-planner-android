package com.pnj.saku_planner.kakeibo.presentation.screens.reflection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.yearMonthToString
import com.pnj.saku_planner.kakeibo.presentation.models.ReflectionUi
import java.time.format.TextStyle

@Composable
fun ReflectionListScreen(
    modifier: Modifier = Modifier,
    reflections: List<ReflectionUi> = emptyList(),
    onCreateReflectionClick: () -> Unit = {},
    onReflectionClick: (String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.reflections),
                    style = Typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                PrimaryButton(onClick = onCreateReflectionClick) {
                    Text(stringResource(R.string.create_reflection))
                }
            }
            reflections.forEach { reflection ->
                Card(
                    modifier = Modifier.clickable { onReflectionClick(reflection.id) }
                ) {
                    Text(
                        text = yearMonthToString(reflection.yearMonth, TextStyle.FULL),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReflectionListScreenPreview() {
    KakeiboTheme {
        ReflectionListScreen()
    }
}
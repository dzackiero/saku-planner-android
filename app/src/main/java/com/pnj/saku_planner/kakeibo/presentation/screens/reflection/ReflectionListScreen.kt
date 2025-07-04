package com.pnj.saku_planner.kakeibo.presentation.screens.reflection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Confirmable
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.core.util.yearMonthToString
import com.pnj.saku_planner.kakeibo.presentation.models.ReflectionUi
import java.time.format.TextStyle

@Composable
fun ReflectionListScreen(
    modifier: Modifier = Modifier,
    reflections: List<ReflectionUi> = emptyList(),
    onCreateReflectionClick: () -> Unit = {},
    onReflectionClick: (String) -> Unit = {},
    onDeleteReflectionClick: (String) -> Unit = {},
) {
    var expandedReflectionIds by remember { mutableStateOf(emptySet<String>()) }

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
            if (reflections.isEmpty()) {
                Text(
                    text = stringResource(R.string.you_don_t_have_any_reflection_yet),
                    style = Typography.bodyMedium,
                    color = AppColor.MutedForeground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                reflections.forEach { reflection ->
                    val isExpanded = reflection.id in expandedReflectionIds

                    Column(
                        modifier = Modifier
                            .shadow(0.5.dp, RoundedCornerShape(4.dp))
                            .border(1.dp, AppColor.Border, RoundedCornerShape(4.dp))
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .clickable { onReflectionClick(reflection.id) }
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = yearMonthToString(reflection.yearMonth, TextStyle.FULL),
                                    style = Typography.headlineMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                IconButton(onClick = {
                                    expandedReflectionIds = if (isExpanded) {
                                        expandedReflectionIds - reflection.id
                                    } else {
                                        expandedReflectionIds + reflection.id
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = if (isExpanded) {
                                            stringResource(R.string.collapse)
                                        } else {
                                            stringResource(R.string.expand)
                                        },
                                    )
                                }
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .padding(horizontal = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    reflection.currentMonthNote?.let {
                                        if (it.isNotBlank()) {
                                            Column {
                                                Text(
                                                    text = stringResource(R.string.month_reflection),
                                                    style = Typography.titleSmall,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = it,
                                                    style = Typography.bodyMedium,
                                                )
                                            }
                                        }
                                    }
                                    reflection.nextMonthNote?.let {
                                        if (it.isNotBlank()) {
                                            Column {
                                                Text(
                                                    text = stringResource(R.string.next_month_plan),
                                                    style = Typography.titleSmall,
                                                    fontWeight = FontWeight.Medium,
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                                Text(
                                                    text = it,
                                                    style = Typography.bodyMedium,
                                                )
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                    ) {
                                        Confirmable(onConfirmed = {
                                            onDeleteReflectionClick(reflection.id)
                                        }) {
                                            Button(
                                                shape = RoundedCornerShape(2.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = AppColor.Destructive,
                                                    contentColor = Color.White,
                                                ),
                                                onClick = it,
                                            ) {
                                                Text(stringResource(R.string.delete))
                                            }
                                        }
                                    }
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
fun ReflectionListScreenPreview() {
    KakeiboTheme {
        ReflectionListScreen(
            reflections = listOf(
                ReflectionUi(
                    id = "1",
                    currentMonthNote = "I saved a lot this month!",
                    nextMonthNote = "I will save more next month!",
                ),
                ReflectionUi(
                    id = "2",
                    currentMonthNote = "I saved a lot this month!",
                    nextMonthNote = "I will save more next month!",
                ),
                ReflectionUi(
                    id = "3",
                    currentMonthNote = "I saved a lot this month!",
                    nextMonthNote = "I will save more next month!",
                )
            )
        )
    }
}
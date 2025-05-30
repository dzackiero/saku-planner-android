package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.formatToCurrency
import com.pnj.saku_planner.kakeibo.presentation.components.ui.yearMonthToString
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionState
import java.time.format.TextStyle

@Composable
fun OverviewPage(
    state: ReflectionState = ReflectionState()
) {
    val incomes =
        state.transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expenses =
        state.transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {

            Text(
                text = stringResource(
                    R.string.overview_title,
                    yearMonthToString(state.yearMonth, TextStyle.FULL)
                ),
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.overview_desc),
                style = Typography.titleMedium,
                color = AppColor.MutedForeground
            )
        }
        Card(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.income),
                    style = Typography.displaySmall
                )

                Text(
                    text = formatToCurrency(incomes),
                    style = Typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = AppColor.Success,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        Card(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.expense),
                    style = Typography.displaySmall
                )

                Text(
                    text = formatToCurrency(expenses),
                    style = Typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = AppColor.Destructive,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        Card(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Net Savings",
                    style = Typography.displaySmall
                )

                Text(
                    text = formatToCurrency((incomes - expenses)),
                    style = Typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = AppColor.Primary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewPagePreview() {
    KakeiboTheme {
        OverviewPage()
    }
}
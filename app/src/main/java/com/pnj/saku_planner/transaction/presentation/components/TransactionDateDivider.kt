package com.pnj.saku_planner.transaction.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.ui.theme.AppColor.MutedForeground
import com.pnj.saku_planner.core.ui.theme.Typography
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.util.Locale

@Composable
fun TransactionDateDivider(date: LocalDate) {
    val formattedDate =
        date.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.getDefault()))
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = formattedDate,
            style = Typography.labelSmall,
            color = MutedForeground,
        )
        HorizontalDivider()
    }
}



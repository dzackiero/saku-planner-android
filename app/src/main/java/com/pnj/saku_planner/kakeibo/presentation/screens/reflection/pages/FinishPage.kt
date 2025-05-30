package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography

@Composable
fun FinishPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Verified,
            contentDescription = "success",
            tint = AppColor.Primary,
            modifier = Modifier
                .size(90.dp)
        )
        Text(
            text = "Ready for Next Month!",
            style = Typography.displayMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Your reflection is complete. Use these insights to make next month even better!",
            color = AppColor.MutedForeground,
            style = Typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FinishPagePreview() {
    KakeiboTheme {
        FinishPage()
    }
}
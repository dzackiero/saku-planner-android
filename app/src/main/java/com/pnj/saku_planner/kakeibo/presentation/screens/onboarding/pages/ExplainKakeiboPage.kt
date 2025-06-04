package com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography

@Composable
fun ExplainKakeiboPage() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = "The Heart of Kakeibo: 4 Questions for Financial Mindfulness",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
        )


        Text(
            text = "1. How much money do I have?",
            style = Typography.displaySmall,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "2. How much would you like to save?",
            style = Typography.displaySmall,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "3. How much do I need to spend?",
            style = Typography.displaySmall,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "4. How can I Improve?",
            style = Typography.displaySmall,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExplainKakeiboPagePreview() {
    KakeiboTheme {
        ExplainKakeiboPage()
    }
}
package com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
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
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton

@Composable
fun ReflectionStartPage() {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.size(1.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = "calendar",
                tint = AppColor.Primary,
                modifier = Modifier
                    .size(90.dp)
            )
            Text(
                text = "Time to Reflect on April 2025!",
                style = Typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Let's see how things went last month. Remember, this is about learning, not judging.",
                color = AppColor.MutedForeground,
                style = Typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
        PrimaryButton(
            onClick = {},
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth()
        ) {
            Text("Start Reflection")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartPagePreview() {
    KakeiboTheme {
        ReflectionStartPage()
    }
}
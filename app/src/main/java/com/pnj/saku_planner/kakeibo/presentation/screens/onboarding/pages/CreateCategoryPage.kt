package com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.screens.settings.CategoryScreen

@Composable
fun CreateCategoryPage() {
    Column {
        Text(
            text = stringResource(R.string.your_transaction_categories),
            textAlign = TextAlign.Center,
            style = Typography.displaySmall,
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 24.dp,
                )
                .fillMaxWidth(),
        )
        CategoryScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCategoryPagePreview() {
    KakeiboTheme {
        CreateCategoryPage()
    }
}

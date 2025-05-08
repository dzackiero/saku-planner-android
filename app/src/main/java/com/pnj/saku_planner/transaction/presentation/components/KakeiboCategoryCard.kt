package com.pnj.saku_planner.transaction.presentation.components

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.InputChip
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.transaction.domain.enum.KakeiboCategory
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.Typography

@Composable
fun KakeiboCard(
    selected: Boolean,
    kakeiboCategory: KakeiboCategory,
    modifier: Modifier = Modifier,
    onClick: (KakeiboCategory) -> Unit,
) {
    InputChip(
        selected = selected,
        onClick = { onClick(kakeiboCategory) },
        modifier = modifier,
        label = {
            val style = kakeiboCategory.getStyle()

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = style.text,
                    modifier = Modifier.size(48.dp),
                    tint = style.color,
                )
                Text(
                    text = style.text,
                    style = Typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = style.subtext,
                    style = Typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AppColor.MutedForeground
                )
            }
        })
}

@Preview
@Composable
fun KakeiboCardPreview() {
    KakeiboCard(
        selected = false,
        kakeiboCategory = KakeiboCategory.NEEDS,
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}


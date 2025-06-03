package com.pnj.saku_planner.kakeibo.presentation.components

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SelectChip

@Composable
fun KakeiboCard(
    selected: Boolean,
    kakeiboCategoryType: KakeiboCategoryType,
    modifier: Modifier = Modifier,
    onClick: (KakeiboCategoryType) -> Unit,
) {
    SelectChip(
        selected = selected,
        onClick = { onClick(kakeiboCategoryType) },
        modifier = modifier,
        label = {
            val style = kakeiboCategoryType.getStyle()

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = style.text,
                    modifier = Modifier.size(36.dp),
                    tint = style.color,
                )
                Text(
                    text = style.text,
                    style = Typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = style.subtext,
                    style = Typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = AppColor.MutedForeground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,

                )
            }
        })
}

@Preview
@Composable
fun KakeiboCardPreview() {
    KakeiboCard(
        selected = false,
        kakeiboCategoryType = KakeiboCategoryType.NEEDS,
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}


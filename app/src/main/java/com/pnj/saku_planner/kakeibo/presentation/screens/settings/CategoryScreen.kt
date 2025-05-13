package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.pnj.saku_planner.core.ui.components.Card
import com.pnj.saku_planner.core.ui.components.DefaultForm
import com.pnj.saku_planner.core.ui.components.PrimaryButton
import com.pnj.saku_planner.core.ui.theme.AppColor
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.ui.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi

@Composable
fun CategoryScreen() {
    val categories = listOf(
        CategoryUi(
            id = 1,
            name = "Food",
            icon = "🍇",
            categoryType = TransactionType.EXPENSE,
        ), CategoryUi(
            id = 2,
            name = "Fiking",
            icon = "🍇",
            categoryType = TransactionType.INCOME,
        )
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Categories",
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            PrimaryButton(onClick = { }) {
                Text("Add Category")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                categories.forEachIndexed { index, category ->
                    CategoryItem(
                        category = category,
                        onClick = { }
                    )

                    if (categories.lastIndex != index) {
                        HorizontalDivider(color = AppColor.Border)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryUi,
    onClick: () -> Unit,
) {
    val icon = when (category.categoryType) {
        TransactionType.INCOME -> Icons.AutoMirrored.Filled.TrendingUp
        TransactionType.EXPENSE -> Icons.AutoMirrored.Filled.TrendingDown
        TransactionType.TRANSFER -> Icons.AutoMirrored.Filled.TrendingFlat
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            category.icon?.let {
                Text(
                    text = it, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                text = category.name, style = Typography.bodyMedium
            )
        }
        Icon(
            icon,
            category.categoryType.toString().lowercase(),
            tint = if (category.categoryType == TransactionType.INCOME) AppColor.Success else AppColor.Destructive
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPreview() {
    SakuPlannerTheme {
        CategoryScreen()
    }
}
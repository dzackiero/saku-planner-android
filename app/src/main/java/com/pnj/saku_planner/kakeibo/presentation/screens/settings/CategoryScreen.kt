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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.TopAppBar
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.SakuPlannerTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.components.ui.Card
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.models.CategoryUi

@Composable
fun CategoryScreen(
    categories: List<CategoryUi> = emptyList(),
    onAddCategoryClicked: () -> Unit = {},
    onCategoryItemClicked: (CategoryUi) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(paddingValues)
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
                PrimaryButton(onClick = onAddCategoryClicked) {
                    Text("Add Category")
                }
            }

            if (categories.isEmpty()) {
                Text(
                    text = "No categories available",
                    style = Typography.headlineMedium,
                    color = AppColor.MutedForeground,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        categories.forEachIndexed { index, category ->
                            CategoryItem(
                                category = category,
                                onClick = {
                                    onCategoryItemClicked(category)
                                }
                            )

                            if (categories.lastIndex != index) {
                                HorizontalDivider(color = AppColor.Border)
                            }
                        }
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
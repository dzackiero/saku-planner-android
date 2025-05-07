package com.pnj.saku_planner.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.ui.theme.AppColor
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultForm(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    title: String,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColor.PrimaryForeground),
                modifier = Modifier
                    .border(1.dp, AppColor.Border)
                    .shadow(0.5.dp),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier.padding(paddingValues)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDefaultForm() {
    SakuPlannerTheme {
        DefaultForm(
            title = "New Transaction"
        ) {
        }
    }
}
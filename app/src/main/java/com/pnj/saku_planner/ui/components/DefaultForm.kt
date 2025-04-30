package com.pnj.saku_planner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.ui.theme.AppColor
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultForm(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
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
                    IconButton(onClick = { /*TODO*/ }) {
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
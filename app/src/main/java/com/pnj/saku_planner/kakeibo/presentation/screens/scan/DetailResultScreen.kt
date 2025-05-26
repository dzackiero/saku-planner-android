package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.models.ScanUi

@Composable
fun DetailResultScreen(
    modifier: Modifier = Modifier,
    items: List<ScanUi> = emptyList(),
    scanViewModel: ScanViewModel,
    navigateToBack: () -> Unit = {},
    navigateToHome: () -> Unit = {}
){
    val items by scanViewModel.items.collectAsState()
    Column(

    ){

    }
}
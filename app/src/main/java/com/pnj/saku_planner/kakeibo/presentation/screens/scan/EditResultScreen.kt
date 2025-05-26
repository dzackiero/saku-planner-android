package com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.models.ScanUi

@Composable
fun EditResultScreen(
    modifier: Modifier = Modifier,
    items: List<ScanUi> = emptyList(),
    scanViewModel: ScanViewModel,
    navigateToDetail: () -> Unit = {},
    navigateToBack: () -> Unit = {}
){
    Column(

    ){

    }
}

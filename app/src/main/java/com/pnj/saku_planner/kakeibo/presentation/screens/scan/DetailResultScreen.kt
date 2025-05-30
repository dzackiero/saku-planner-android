package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.models.ScanUi
import com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels.TransactionFormViewModel

@Composable
fun DetailResultScreen(
    modifier: Modifier = Modifier,
    items: List<ScanUi> = emptyList(),
    transactionFormViewModel: TransactionFormViewModel,
    navigateToHome: () -> Unit = {}
){
    Column(

    ){

    }
}
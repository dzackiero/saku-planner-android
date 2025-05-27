package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel

@Composable
fun SummaryResultScreen(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel,
    navigateToDetail: () -> Unit = {},
    navigateToEdit: () -> Unit = {}
){
    val totalPrice by scanViewModel.totalPrice.collectAsState()
    val tax by scanViewModel.tax.collectAsState()
    val items by scanViewModel.items.collectAsState()
    Column(

    ){
        totalPrice?.let { Text(it) }
        tax?.let { Text(it) }
        items?.let { itemList ->
            Column {
                itemList.forEach { item ->
                    Text(text = "${item.itemName}: Rp${item.price}")
                }
            }
        }
    }
}
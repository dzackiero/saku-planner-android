package com.pnj.saku_planner.kakeibo.presentation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.kakeibo.presentation.screens.report.SummaryScreen
import com.pnj.saku_planner.kakeibo.presentation.screens.report.viewmodels.SummaryViewModel

@Composable
fun SummaryRoute() {
    val viewModel = hiltViewModel<SummaryViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SummaryScreen(
        state = state,
        callback = viewModel.callbacks,
    )
}
package com.pnj.saku_planner.kakeibo.presentation.screens.reflection

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.BudgetPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.CategoryPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.FinishPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.KakeiboPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.OverviewPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.ReflectionPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.SavingPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.pages.StartPage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.viewmodels.ReflectionViewModel

// Assuming ReflectionState has properties like 'budgets' and 'savings'
// e.g., data class ReflectionState(..., val budgets: List<Any>?, val savings: List<Any>?)

@Composable
fun ReflectionScreen(
    navigateBack: () -> Unit = {},
    navigateOnFinish: () -> Unit = {}
) {
    val viewModel = hiltViewModel<ReflectionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var currentStep by rememberSaveable { mutableIntStateOf(0) }
    val steps = listOf(
        FormStep("Start") { StartPage(state) },
        FormStep("Overview") { OverviewPage(state) },
        FormStep("Category") { CategoryPage(state) },
        FormStep("Kakeibo") { KakeiboPage(state, viewModel.callbacks) },
        FormStep("Budget") { BudgetPage(state) }, // Remains in list for progress calculation
        FormStep("Saving") { SavingPage(state, viewModel.callbacks) }, // Remains in list
        FormStep("Reflection") { ReflectionPage(state, viewModel.callbacks) },
        FormStep("Finish") { FinishPage() }
    )

    fun addStep() {
        if (currentStep >= steps.size - 1) { // Already at the last step ("Finish")
            navigateOnFinish()
            return
        }

        var nextActualStep = currentStep + 1

        // Loop to find the next non-skippable step, but don't skip the "Finish" page
        while (nextActualStep < steps.size - 1) {
            val stepName = steps[nextActualStep].name

            val budgetsEmpty = state.budgets.isEmpty()
            val savingsEmpty = state.savings.isEmpty()

            val shouldSkip = (stepName == "Budget" && budgetsEmpty) ||
                    (stepName == "Saving" && savingsEmpty)

            if (shouldSkip) {
                nextActualStep++
            } else {
                break
            }
        }
        currentStep = nextActualStep
    }

    fun removeStep() {
        if (currentStep <= 0) { // Already at the first step ("Start")
            navigateBack()
            return
        }

        var prevActualStep = currentStep - 1

        while (prevActualStep > 0) {
            val stepName = steps[prevActualStep].name

            val budgetsEmpty = state.budgets.isEmpty()
            val savingsEmpty = state.savings.isEmpty()

            val shouldSkip = (stepName == "Budget" && budgetsEmpty) ||
                    (stepName == "Saving" && savingsEmpty)

            if (shouldSkip) {
                prevActualStep--
            } else {
                break
            }
        }
        currentStep = prevActualStep
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (currentStep + 1).toFloat() / steps.size.toFloat(),
        label = "ProgressAnimation"
    )

    Scaffold {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LinearProgressIndicator(
                progress = { animatedProgress }, modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(12.dp)
                    .fillMaxWidth()
            )

            Crossfade(
                targetState = currentStep,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = "StepCrossfade"
            ) { stepIndex ->
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    steps[stepIndex].content()
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SecondaryButton(
                    onClick = { removeStep() }, modifier = Modifier.width(120.dp)
                ) {
                    if (currentStep == 0)
                        Text(stringResource(R.string.cancel))
                    else
                        Text(stringResource(R.string.previous))
                }
                PrimaryButton(
                    onClick = { addStep() }, modifier = Modifier.width(120.dp)
                ) {
                    val nextButtonText =
                        if (currentStep == steps.size - 1)
                            stringResource(R.string.finish)
                        else
                            stringResource(R.string.next)

                    Text(nextButtonText)
                }
            }
        }
    }
}

data class FormStep(
    val name: String,
    val content: @Composable () -> Unit
)

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ReflectionScreenPreview() {
    KakeiboTheme {
        ReflectionScreen()
    }
}
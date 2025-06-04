package com.pnj.saku_planner.kakeibo.presentation.screens.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages.CreateAccountPage
import com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages.CreateBudgetPage
import com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages.CreateCategoryPage
import com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages.ExplainKakeiboPage
import com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages.WelcomePage
import com.pnj.saku_planner.kakeibo.presentation.screens.reflection.ScreenStep

@Composable
fun OnboardingScreen(
    navigateBack: () -> Unit = {},
    navigateOnFinish: () -> Unit = {}
) {
    var currentStep by rememberSaveable { mutableIntStateOf(1) }
    val steps = listOf(
        ScreenStep("Start") { WelcomePage() },
        ScreenStep("Kakeibo") { ExplainKakeiboPage() },
        ScreenStep("Account") { CreateAccountPage() },
        ScreenStep("category") { CreateCategoryPage() },
        ScreenStep("budget") { CreateBudgetPage() },
    )

    fun addStep() {
        if (currentStep >= steps.size - 1) {
            navigateOnFinish()
            return
        }

        var nextActualStep = currentStep + 1

        while (nextActualStep < steps.size - 1) {
            val stepName = steps[nextActualStep].name
            val shouldSkip = false

            if (shouldSkip) {
                nextActualStep++
            } else {
                break
            }
        }
        currentStep = nextActualStep
    }

    fun removeStep() {
        if (currentStep <= 0) {
            navigateBack()
            return
        }

        var prevActualStep = currentStep - 1

        while (prevActualStep > 0) {
            val stepName = steps[prevActualStep].name
            val shouldSkip = false

            if (shouldSkip) {
                prevActualStep--
            } else {
                break
            }
        }
        currentStep = prevActualStep
    }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    IconButton(onClick = { removeStep() }) {
                        Icon(Icons.Default.ArrowBackIosNew, "back")
                    }
                } else {
                    Spacer(Modifier.size(24.dp))
                }

                TextButton(onClick = navigateBack) {
                    Text(text = stringResource(R.string.skip))
                }
            }

            Crossfade(
                targetState = currentStep,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
                horizontalArrangement = Arrangement.Center,
            ) {
                PrimaryButton(
                    onClick = { addStep() }, modifier = Modifier.fillMaxWidth()
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

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    KakeiboTheme {
        OnboardingScreen()
    }
}


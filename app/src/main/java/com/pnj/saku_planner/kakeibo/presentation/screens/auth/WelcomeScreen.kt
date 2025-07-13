package com.pnj.saku_planner.kakeibo.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.components.ui.SecondaryButton
import kotlinx.coroutines.delay

fun <T> getAnimationSpec(
    durationMillis: Int,
    skipped: Boolean,
    easing: Easing = FastOutSlowInEasing
): FiniteAnimationSpec<T> {
    return tween(
        durationMillis = if (skipped) 0 else durationMillis,
        easing = if (skipped) LinearEasing else easing
    )
}

fun getScaleAnimationSpec(
    durationMillis: Int,
    skipped: Boolean
): FiniteAnimationSpec<Float> {
    val customEasing = Easing { it * it }
    return tween(
        durationMillis = if (skipped) 0 else durationMillis,
        easing = if (skipped) LinearEasing else customEasing
    )
}

@Composable
fun WelcomeScreen(
    onLoginClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {},
    onUseOfflineClicked: () -> Unit = {},
) {
    // State to track if animations should be skipped
    var animationsSkipped by remember { mutableStateOf(false) }

    // State variables to drive the animation sequence if not skipped
    var showTitleProgress by remember { mutableStateOf(false) }
    var finishedScaleProgress by remember { mutableStateOf(false) }
    var showSubtitleProgress by remember { mutableStateOf(false) }
    var showDescriptionProgress by remember { mutableStateOf(false) }
    var showButtonsProgress by remember { mutableStateOf(false) }

    // Effective states for UI elements, considering the skip flag
    // If animationsSkipped is true, these will all evaluate to true immediately
    val showTitle = showTitleProgress || animationsSkipped
    val finishedScale = finishedScaleProgress || animationsSkipped
    val showSubtitle = showSubtitleProgress || animationsSkipped
    val showDescription = showDescriptionProgress || animationsSkipped
    val showButtons = showButtonsProgress || animationsSkipped

    // Animate scale: uses 'showTitle' which incorporates 'animationsSkipped'
    // Animation spec duration becomes 0 if 'animationsSkipped' is true
    val scale by animateFloatAsState(
        targetValue = if (showTitle) 1f else 0.4f,
        animationSpec = getScaleAnimationSpec(1000, animationsSkipped),
        label = "onboardingTitleScale"
    )

    // Animate title offset: uses 'finishedScale'
    val titleOffset by animateDpAsState(
        targetValue = if (finishedScale) (-150).dp else 0.dp,
        animationSpec = getAnimationSpec(
            1000,
            animationsSkipped
        ), // Uses default FastOutSlowInEasing if not skipped
        label = "onboardingTitleOffset"
    )

    // This LaunchedEffect orchestrates the sequence of animations.
    // It runs once when the composable enters the composition.
    // If 'animationsSkipped' becomes true, the checks within will cause it to exit early.
    LaunchedEffect(Unit) {
        // If somehow already skipped (e.g., very fast interaction or a persisted state), do nothing.
        if (animationsSkipped) return@LaunchedEffect

        showTitleProgress = true
        delay(1200) // Original delay for title to show and scale
        if (animationsSkipped) return@LaunchedEffect // Check before next step

        finishedScaleProgress = true // Title starts moving up
        delay(1500) // Original delay for title to finish moving
        if (animationsSkipped) return@LaunchedEffect

        showSubtitleProgress = true
        delay(1000)
        if (animationsSkipped) return@LaunchedEffect

        showDescriptionProgress = true
        delay(1000)
        if (animationsSkipped) return@LaunchedEffect

        showButtonsProgress = true
    }


    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            // Add click listener to the entire screen
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    if (!animationsSkipped) {
                        animationsSkipped = true
                        // This state change will:
                        // 1. Trigger recomposition.
                        // 2. Cause derived 'showXxx' vars to become true.
                        // 3. Change animation specs to 0 duration, snapping them to final states.
                        // 4. Cause the LaunchedEffect to exit on its next 'if (animationsSkipped)' check.
                    }
                })
            }
    ) {
        // 1) Title Block: Scaled and offset
        // Uses 'scale' and 'titleOffset' which are already aware of 'animationsSkipped'
        Column(
            Modifier
                .align(Alignment.Center)
                .offset { IntOffset(0, titleOffset.roundToPx()) }
                .graphicsLayer { scaleX = scale; scaleY = scale },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.onboarding_welcome),
                style = Typography.displayMedium,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Text(
                stringResource(R.string.onboarding_title),
                style = Typography.displayMedium,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        }

        // 2) Subtitle: Fades in
        // 'visible' uses 'showSubtitle' (derived from 'animationsSkipped')
        // 'enter' animation spec duration becomes 0 if skipped
        AnimatedVisibility(
            visible = showSubtitle,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-72).dp),
            enter = fadeIn(animationSpec = getAnimationSpec(500, animationsSkipped))
        ) {
            Text(
                stringResource(R.string.onboarding_subtitle),
                style = Typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        // 3) Description: Fades in
        AnimatedVisibility(
            visible = showDescription,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (24).dp),
            enter = fadeIn(animationSpec = getAnimationSpec(500, animationsSkipped))
        ) {
            Text(
                stringResource(R.string.onboarding_description),
                style = Typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        AnimatedVisibility(
            visible = showButtons,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = getAnimationSpec(500, animationsSkipped)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                PrimaryButton(
                    Modifier.fillMaxWidth(),
                    onClick = onRegisterClicked
                ) {
                    Text(stringResource(R.string.create_new_account))
                }
                SecondaryButton(
                    Modifier.fillMaxWidth(),
                    onClick = onLoginClicked
                ) {
                    Text(stringResource(R.string.login))
                }
                SecondaryButton(
                    Modifier.fillMaxWidth(),
                    onClick = onUseOfflineClicked
                ) {
                    Text(stringResource(R.string.continue_without_account))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    KakeiboTheme {
        WelcomeScreen()
    }
}

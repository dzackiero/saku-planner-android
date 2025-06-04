package com.pnj.saku_planner.kakeibo.presentation.screens.onboarding.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.getAnimationSpec
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.getScaleAnimationSpec
import kotlinx.coroutines.delay

@Composable
fun WelcomePage(
    modifier: Modifier = Modifier
) {
    var animationsSkipped by remember { mutableStateOf(true) }

    var showTitleProgress by remember { mutableStateOf(false) }
    var finishedScaleProgress by remember { mutableStateOf(false) }
    var showSubtitleProgress by remember { mutableStateOf(false) }
    var showDescriptionProgress by remember { mutableStateOf(false) }

    val showTitle = showTitleProgress || animationsSkipped
    val finishedScale = finishedScaleProgress || animationsSkipped
    val showSubtitle = showSubtitleProgress || animationsSkipped
    val showDescription = showDescriptionProgress || animationsSkipped

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

    LaunchedEffect(Unit) {
        if (animationsSkipped) return@LaunchedEffect

        showTitleProgress = true
        delay(1200)
        if (animationsSkipped) return@LaunchedEffect

        finishedScaleProgress = true
        delay(1500)
        if (animationsSkipped) return@LaunchedEffect

        showSubtitleProgress = true
        delay(1000)
        if (animationsSkipped) return@LaunchedEffect

        showDescriptionProgress = true
        delay(1000)
        if (animationsSkipped) return@LaunchedEffect
    }


    Box(
        modifier = modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    if (!animationsSkipped) {
                        animationsSkipped = true

                    }
                })
            }
    ) {
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
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
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
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePagePreview() {
    KakeiboTheme {
        WelcomePage()
    }
}

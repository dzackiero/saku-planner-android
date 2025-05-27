package com.pnj.saku_planner.kakeibo.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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

@Composable
fun OnboardingScreen(
    onLoginClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {},
) {
    var showTitle by remember { mutableStateOf(false) }
    var finishedScale by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showDescription by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    // 1s scale animation
    val scale by animateFloatAsState(
        targetValue = if (showTitle) 1f else 0.4f,
        animationSpec = tween(durationMillis = 1000, easing = { it * it }),
    )

    // move title up once scale is done
    val titleOffset by animateDpAsState(
        targetValue = if (finishedScale) (-150).dp else 0.dp,
        animationSpec = tween(durationMillis = 1000),
    )

    LaunchedEffect(Unit) {
        showTitle = true
        delay(1200)
        finishedScale = true
        delay(1500)
        showSubtitle = true
        delay(1000)
        showDescription = true
        delay(1000)
        showButtons = true
    }


    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1) Title Block: only this is scaled + offset
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

        // 2) Subtitle: fixed position below center
        AnimatedVisibility(
            visible = showSubtitle,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-72).dp),
            enter = fadeIn(tween(500))
        ) {
            Text(
                stringResource(R.string.onboarding_subtitle),
                style = Typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        // 3) Description: fixed a bit further down
        AnimatedVisibility(
            visible = showDescription,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (24).dp),
            enter = fadeIn(tween(500))
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
                // start the slide from the full height of the container
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 500)
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
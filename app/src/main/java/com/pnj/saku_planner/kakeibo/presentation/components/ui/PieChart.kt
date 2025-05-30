package com.pnj.saku_planner.kakeibo.presentation.components.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.pnj.saku_planner.core.theme.TailwindColor
import com.pnj.saku_planner.core.theme.Typography
import timber.log.Timber
import java.util.Locale
import kotlin.math.*

@Composable
fun PieChartWithText(
    chartDataList: List<ChartData> = listOf(
        ChartData("Utilities", TailwindColor.Yellow400, 200000),
        ChartData("Utilities", TailwindColor.Red400, 200000),
        ChartData("Utilities", TailwindColor.Blue400, 200000),
        ChartData("Utilities", TailwindColor.Green400, 200000)
    ),
    startupAnimation: Boolean = true,
    totalLabel: String? = null,
    totalFormatter: (Long) -> String = { it.toString() },
    onSegmentClick: (ChartData) -> Unit = {}
) {
    // remember selected segment index
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val animationProgress = remember { Animatable(if (startupAnimation) 0f else 1f) }
    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }
    val progress = animationProgress.value

    val total = chartDataList.sumOf { it.value }
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()


    BoxWithConstraints(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val canvasSize = with(density) { min(maxWidth, maxHeight).toPx() }
        val radius = canvasSize * 0.2f
        val strokeWidth = radius * 0.6f
        val outerRadius = radius + strokeWidth
        val diameter = (radius + strokeWidth) * 1.8f
        val topLeft = (canvasSize - diameter) / 2f


        val labelLayouts = remember(chartDataList) {
            chartDataList.map { data ->
                textMeasurer.measure(
                    text = data.label.orEmpty(),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        val percentLayouts = remember(chartDataList, total) {
            chartDataList.map { data ->
                val pct = data.value.toDouble() / total.toDouble() * 100.0
                Timber.d("Calculating percentage for: ${data.label} with value: ${data.value} and total: $total = $pct")
                val formatted = String.format(Locale.getDefault(), "%.2f", pct)
                textMeasurer.measure(
                    text = "$formatted%",
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)
                )
            }
        }
        val totalLayout = remember(total) {
            textMeasurer.measure(
                text = totalFormatter(total),
                style = if (total > 10_000_000) Typography.titleSmall else Typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                constraints = Constraints(maxWidth = (radius * 2).toInt())
            )
        }
        val totalLabelLayout = remember(totalLabel) {
            totalLabel?.let {
                textMeasurer.measure(
                    text = it,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
                )
            }
        }

        Canvas(
            modifier = Modifier
                .size(with(density) { canvasSize.toDp() })
                .pointerInput(chartDataList) {
                    detectTapGestures { offset ->
                        val dx = offset.x - size.center.x
                        val dy = offset.y - size.center.y
                        val dist = sqrt(dx * dx + dy * dy)
                        if (dist in radius..(radius + strokeWidth)) {
                            val angle = (atan2(dy, dx).toDegrees() + 360) % 360
                            var current = 0f
                            chartDataList.forEachIndexed { idx, data ->
                                val fullSweep = (data.value / total * 360).toFloat()
                                val sweep = fullSweep * progress
                                if (angle in current..(current + sweep)) {
                                    // toggle selection
                                    selectedIndex = if (selectedIndex == idx) null else idx
                                    onSegmentClick(data)
                                    return@detectTapGestures
                                }
                                current += sweep
                            }
                        }
                    }
                }
        ) {
            var startAngle = 0f
            chartDataList.forEachIndexed { index, data ->
                val fullSweep = if (total != 0L) {
                    (data.value.toDouble() / total.toDouble() * 360.0).toFloat()
                } else {
                    0f
                }
                val sweepAngle = fullSweep * progress

                // draw arc
                drawArc(
                    color = data.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(topLeft, topLeft),
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth)
                )

                val percentageOfTotal = if (total != 0L) {
                    data.value.toDouble() / total.toDouble()
                } else {
                    0.0
                }
                // draw label/percentage only if none selected or this is selected
                if ((selectedIndex == null || selectedIndex == index) && animationProgress.value >= 1f && percentageOfTotal > 0.02) {
                    val midAngle = startAngle + sweepAngle / 2
                    val angleRad = midAngle.degreeToAngle
                    val blockCenter = Offset(
                        x = center.x + (outerRadius + 22.dp.toPx()) * cos(angleRad),
                        y = center.y + (outerRadius + 22.dp.toPx()) * sin(angleRad)
                    )

                    val lblLayout = labelLayouts[index]
                    val pctLayout = percentLayouts[index]

                    if (midAngle in 180f..360f) {
                        // bottom half: inline pct, label above
                        val pctOff = getTextOffsets(
                            midAngle,
                            IntSize(pctLayout.size.width, pctLayout.size.height)
                        )
                        drawText(pctLayout, topLeft = blockCenter + pctOff)
                        val lblOffRaw = getTextOffsets(
                            midAngle,
                            IntSize(lblLayout.size.width, lblLayout.size.height)
                        )
                        val lblOff = lblOffRaw + Offset(0f, -pctLayout.size.height.toFloat())
                        drawText(lblLayout, topLeft = blockCenter + lblOff)
                    } else {
                        // top half: label then pct
                        val lblOff = getTextOffsets(
                            midAngle,
                            IntSize(lblLayout.size.width, lblLayout.size.height)
                        )
                        drawText(lblLayout, topLeft = blockCenter + lblOff)
                        val pctOffRaw = getTextOffsets(
                            midAngle,
                            IntSize(pctLayout.size.width, pctLayout.size.height)
                        )
                        val pctOff = pctOffRaw + Offset(0f, lblLayout.size.height.toFloat())
                        drawText(pctLayout, topLeft = blockCenter + pctOff)
                    }
                }

                startAngle += sweepAngle
            }

            totalLabelLayout?.let { lbl ->
                val lblSize = lbl.size
                val totalSize = totalLayout.size
                val lblOffset = Offset(
                    x = center.x - lblSize.width / 2f,
                    y = center.y - lblSize.height - 4.dp.toPx() - totalSize.height / 2f
                )
                drawText(lbl, topLeft = lblOffset)
            }

            // Draw total in center
            val txtSize = totalLayout.size
            val centerOffset = Offset(
                x = center.x - txtSize.width / 2f,
                y = center.y - txtSize.height / 2f
            )
            drawText(totalLayout, topLeft = centerOffset)
        }
    }
}

// Helpers
private fun Float.toDegrees(): Float = Math.toDegrees(this.toDouble()).toFloat()
private val Float.degreeToAngle: Float get() = (this * Math.PI / 180f).toFloat()

private fun getTextOffsets(startAngle: Float, textSize: IntSize): Offset {
    val (w, h) = textSize
    val offX: Int
    val offY: Int
    when (startAngle) {
        in 0f..90f -> {
            offX = if (startAngle < 60f) 0 else (-w / 2 * ((startAngle - 60) / 30)).toInt(); offY =
                0
        }

        in 90f..180f -> {
            offX = (-w / 2 - w / 2 * (startAngle - 90) / 45).toInt().coerceAtLeast(-w); offY =
                if (startAngle < 135f) 0 else (-h / 2 * ((startAngle - 135) / 45)).toInt()
        }

        in 180f..270f -> {
            offX =
                if (startAngle < 240f) -w else (-w + w / 2 * (startAngle - 240) / 30).toInt(); offY =
                if (startAngle < 225f) (-h / 2 * ((startAngle - 135) / 45)).toInt() else -h
        }

        else -> {
            offX =
                if (startAngle < 315f) (-w / 2 + w / 2 * (startAngle - 270) / 45).toInt() else 0; offY =
                if (startAngle < 315f) -h else (-h + h * (startAngle - 315) / 45).toInt()
        }
    }
    return Offset(offX.toFloat(), offY.toFloat())
}

@Immutable
data class ChartData(val label: String?, val color: Color, val value: Long)

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    PieChartWithText(startupAnimation = false) { clicked -> println("Clicked: ${'$'}${clicked.label}") }
}

package com.prismforge.visualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VisualizerCanvas(state: VisualizerState) {

    LaunchedEffect(Unit) {
        while (true) {
            if (state.playing) {
                state.rotation += 0.04
            }
            delay(16)
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val scale = minOf(size.width, size.height) * 0.28f

        val glowColor = Color(
            red = 0.1f,
            green = 0.8f,
            blue = 1f,
            alpha = 0.8f
        )

        // Glow
        drawCircle(
            color = glowColor.copy(alpha = state.glow * 0.12f),
            radius = scale * 1.4f,
            center = Offset(cx, cy)
        )

        when (state.selectedObject) {

            VisualObject.Cube -> {
                val points = listOf(
                    floatArrayOf(-1f, -1f, -1f),
                    floatArrayOf(1f, -1f, -1f),
                    floatArrayOf(1f, 1f, -1f),
                    floatArrayOf(-1f, 1f, -1f),
                    floatArrayOf(-1f, -1f, 1f),
                    floatArrayOf(1f, -1f, 1f),
                    floatArrayOf(1f, 1f, 1f),
                    floatArrayOf(-1f, 1f, 1f)
                )

                val projected = points.map {
                    projectPoint(
                        it[0],
                        it[1],
                        it[2],
                        state.rotation,
                        scale,
                        cx,
                        cy
                    )
                }

                val edges = listOf(
                    0 to 1, 1 to 2, 2 to 3, 3 to 0,
                    4 to 5, 5 to 6, 6 to 7, 7 to 4,
                    0 to 4, 1 to 5, 2 to 6, 3 to 7
                )

                edges.forEach { (a, b) ->
                    drawLine(
                        color = glowColor,
                        start = projected[a],
                        end = projected[b],
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }

            VisualObject.Sphere -> {
                val radius = scale

                for (i in 0..10) {
                    val y = -1f + i * 0.2f
                    val r = kotlin.math.sqrt(
                        (1f - y * y).coerceAtLeast(0f)
                    )

                    val path = Path()

                    for (j in 0..80) {
                        val angle = j * Math.PI * 2 / 80
                        val x = r * cos(angle).toFloat()
                        val z = r * sin(angle).toFloat()

                        val p = projectPoint(
                            x,
                            y,
                            z,
                            state.rotation,
                            radius,
                            cx,
                            cy
                        )

                        if (j == 0) {
                            path.moveTo(p.x, p.y)
                        } else {
                            path.lineTo(p.x, p.y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = glowColor,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                for (i in 0..12) {
                    val longitude = i * Math.PI * 2 / 12

                    val path = Path()

                    for (j in 0..80) {
                        val latitude =
                            -Math.PI / 2 + j * Math.PI / 80

                        val x =
                            cos(latitude) * cos(longitude)

                        val y =
                            sin(latitude)

                        val z =
                            cos(latitude) * sin(longitude)

                        val p = projectPoint(
                            x.toFloat(),
                            y.toFloat(),
                            z.toFloat(),
                            state.rotation,
                            radius,
                            cx,
                            cy
                        )

                        if (j == 0) {
                            path.moveTo(p.x, p.y)
                        } else {
                            path.lineTo(p.x, p.y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = glowColor,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            VisualObject.Torus -> {
                val majorRadius = scale
                val minorRadius = scale * 0.32f

                for (i in 0..32) {
                    val u = i * Math.PI * 2 / 32
                    val path = Path()

                    for (j in 0..40) {
                        val v = j * Math.PI * 2 / 40

                        val x =
                            (majorRadius / scale +
                                    minorRadius / scale * cos(v)) * cos(u)

                        val y =
                            minorRadius / scale * sin(v)

                        val z =
                            (majorRadius / scale +
                                    minorRadius / scale * cos(v)) * sin(u)

                        val p = projectPoint(
                            x.toFloat(),
                            y.toFloat(),
                            z.toFloat(),
                            state.rotation,
                            scale,
                            cx,
                            cy
                        )

                        if (j == 0) {
                            path.moveTo(p.x, p.y)
                        } else {
                            path.lineTo(p.x, p.y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = glowColor,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }
    }
}

private fun projectPoint(
    x: Float,
    y: Float,
    z: Float,
    rotation: Double,
    scale: Float,
    cx: Float,
    cy: Float
): Offset {

    val cosR = cos(rotation).toFloat()
    val sinR = sin(rotation).toFloat()

    val rotatedX = x * cosR - z * sinR
    val rotatedZ = x * sinR + z * cosR

    val perspective =
        1f / (1f + rotatedZ * 0.35f)

    val screenX =
        cx + rotatedX * scale * perspective

    val screenY =
        cy + y * scale * perspective

    return Offset(screenX, screenY)
}

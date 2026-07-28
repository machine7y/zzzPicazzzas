package com.example.zzzpicazzzas.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import com.example.zzzpicazzzas.R

private const val DURATION = 400

@Composable
fun PizzaAnimation(image: ImageBitmap, startAnimation: Boolean, onAnimationEnd: () -> Unit) {
    val progress by animateIntAsState(
        targetValue = if (startAnimation) 360 else 0,
        animationSpec = tween(
            durationMillis = DURATION,
            easing = LinearEasing,
        ),
        label = stringResource(R.string.splash_labelPizzaAnimation),
        finishedListener = { _ -> onAnimationEnd() }
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        val path = createClippingPath(progress)
        val (destinationWidth, destinationHeight) = calculateDestinationSize(image)

        clipPath(path) {
            drawImage(
                image = image,
                dstSize = IntSize(destinationWidth.toInt(), destinationHeight.toInt()),
            )
        }
    }
}

private fun DrawScope.calculateDestinationSize(image: ImageBitmap): Pair<Float, Float> {
    val canvasSize = size.width
    val imageWidth = image.width.toFloat()
    val imageHeight = image.height.toFloat()
    val scale = canvasSize / imageWidth
    val destinationWidth = imageWidth * scale
    val destinationHeight = imageHeight * scale

    return Pair(destinationWidth, destinationHeight)
}

@Suppress("MagicNumber")
private fun DrawScope.createClippingPath(progress: Int): Path {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2
    val sweepAngle = when {
        progress == 0 -> 0f
        progress <= 45 -> 43f
        progress <= 90 -> 86f
        progress <= 135 -> 130f
        progress <= 180 -> 173f
        progress <= 225 -> 220f
        progress <= 270 -> 269f
        progress <= 315 -> 315f
        else -> 360f // progress <= 360
    }
    val path = Path().apply {
        moveTo(center.x, center.y)
        arcTo(
            rect = Rect(
                left = center.x - radius,
                top = center.y - radius,
                right = center.x + radius,
                bottom = center.y + radius,
            ),
            startAngleDegrees = 3f,
            sweepAngleDegrees = sweepAngle,
            forceMoveTo = false,
        )
        close()
    }

    return path
}

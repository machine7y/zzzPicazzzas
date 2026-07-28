package com.example.zzzpicazzzas.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize


@Composable
fun fullScreenWidthDp() = fullScreenSizeDpSize().width

@Composable
fun fullScreenHeightDp() = fullScreenSizeDpSize().height

@Composable
fun fullScreenWidthPx() = fullScreenSizePxSize().width

@Composable
fun fullScreenHeightPx() = fullScreenSizePxSize().height

@Composable
fun fullScreenSizeDpSize(): DpSize {
    val size = fullScreenSizePxSize()

    return DpSize(
        width = size.width.toDp(),
        height = size.height.toDp()
    )
}

@Composable
fun fullScreenSizePxSize(): Size {
    val containerSize = LocalWindowInfo.current.containerSize

    return Size(
        width = containerSize.width.toFloat(),
        height = containerSize.height.toFloat(),
    )
}

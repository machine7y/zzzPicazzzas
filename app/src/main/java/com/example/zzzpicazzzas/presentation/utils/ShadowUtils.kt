package com.example.zzzpicazzzas.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun createShadow(): Shadow = Shadow(
    radius = 12.dp,
    spread = 0.dp,
    color = Color.Black.copy(alpha = 0.20f),
    offset = DpOffset(x = 2.dp, y = 4.dp),
)

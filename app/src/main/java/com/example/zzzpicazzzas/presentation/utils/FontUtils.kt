package com.example.zzzpicazzzas.presentation.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

fun letterSpacingPercentToSp(percent: Float, fontSize: TextUnit, ): TextUnit {
    require(fontSize.isSp) { "fontSize must be in sp" }

    return (fontSize.value * percent / 100f).sp
}

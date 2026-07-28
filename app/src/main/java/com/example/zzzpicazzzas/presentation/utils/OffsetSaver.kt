package com.example.zzzpicazzzas.presentation.utils

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.geometry.Offset

class OffsetSaver: Saver<Offset, List<Float>> {

    override fun SaverScope.save(value: Offset) = listOf(value.x, value.y)

    override fun restore(value: List<Float>) = Offset(x = value[0], y = value[1])
}

package com.example.zzzpicazzzas.presentation.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable

@Composable
fun statusBarTopPaddingDp() = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

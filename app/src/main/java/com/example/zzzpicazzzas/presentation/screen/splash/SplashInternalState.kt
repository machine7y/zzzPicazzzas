package com.example.zzzpicazzzas.presentation.screen.splash

import com.example.zzzpicazzzas.presentation.base.mvvm.BaseInternalState

data class SplashInternalState(
    val isAnimationFinished: Boolean = false,
    val isDataLoaded: Boolean = false,
) : BaseInternalState

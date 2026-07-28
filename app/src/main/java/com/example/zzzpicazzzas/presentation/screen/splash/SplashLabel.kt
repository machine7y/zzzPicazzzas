package com.example.zzzpicazzzas.presentation.screen.splash

import com.example.zzzpicazzzas.presentation.base.mvvm.BaseLabel
import com.example.zzzpicazzzas.presentation.utils.StringResourceWrapper

sealed interface SplashLabel : BaseLabel {

    data class NetworkExceptionMessage(
        val message: StringResourceWrapper,
    ): SplashLabel
}

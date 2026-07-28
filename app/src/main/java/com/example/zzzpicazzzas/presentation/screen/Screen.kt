package com.example.zzzpicazzzas.presentation.screen

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable
    data object SplashScreen : Screen

    @Serializable
    data object DetailScreen : Screen
}

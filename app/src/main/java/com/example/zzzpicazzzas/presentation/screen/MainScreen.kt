package com.example.zzzpicazzzas.presentation.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.arttttt.nav3router.Nav3Host
import com.arttttt.nav3router.Router
import com.example.zzzpicazzzas.presentation.screen.Screen.DetailScreen
import com.example.zzzpicazzzas.presentation.screen.Screen.SplashScreen
import com.example.zzzpicazzzas.presentation.screen.detail.DetailScreen
import com.example.zzzpicazzzas.presentation.screen.splash.SplashScreen
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

private const val ANIMATION_DURATION_IN_MILLIS = 1500
private const val ANIMATION_SCALE = 0.3f

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val screenBackStack = rememberNavBackStack(SplashScreen)
    val router: Router<Screen> =
        EntryPointAccessors.fromApplication(context.applicationContext, RouterEntryPoint::class.java).router()

    Nav3Host(
        backStack = screenBackStack,
        router = router,
    ) { screenBackStack, onBack, _ ->
        NavDisplay(
            backStack = screenBackStack,
            onBack = onBack,
            entryDecorators = listOf(rememberViewModelStoreNavEntryDecorator()),
            entryProvider = entryProvider {
                entry<SplashScreen> {
                    SplashScreen()
                }
                entry<DetailScreen> {
                    DetailScreen()
                }
            },
            transitionSpec = {
                (
                    fadeIn(
                        animationSpec = tween(ANIMATION_DURATION_IN_MILLIS),
                    )
                    ) togetherWith (
                    fadeOut(
                        animationSpec = tween(ANIMATION_DURATION_IN_MILLIS),
                    ) + scaleOut(
                        targetScale = ANIMATION_SCALE,
                        animationSpec = tween(ANIMATION_DURATION_IN_MILLIS),
                    )
                    )
            }
        )
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface RouterEntryPoint {
    fun router(): Router<Screen>
}

package com.example.zzzpicazzzas.presentation.screen.splash

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.zzzpicazzzas.R
import com.example.zzzpicazzzas.presentation.components.PizzaAnimation
import com.example.zzzpicazzzas.presentation.screen.splash.SplashEvent.OnAnimationEnd
import com.example.zzzpicazzzas.presentation.screen.splash.SplashLabel.NetworkExceptionMessage
import com.example.zzzpicazzzas.presentation.utils.asString

@Composable
fun SplashScreen() {
    val viewModel = hiltViewModel<SplashViewModel>()
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    val image = ImageBitmap.imageResource(R.drawable.img_pizza)

    LaunchedEffect(Unit) {
        viewModel.labelFlow.collect { label ->
            when (label) {
                is NetworkExceptionMessage -> {
                    Toast.makeText(context, label.message.asString(context), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SplashContent(
        state = state,
        image = image,
        onAnimationEnd = { viewModel.onEvent(OnAnimationEnd) }
    )
}

@Composable
fun SplashContent(state: SplashState, image: ImageBitmap, onAnimationEnd: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(52.dp),
    ) {
        PizzaAnimation(
            image = image,
            startAnimation = state.isAnimatePizza,
            onAnimationEnd = onAnimationEnd,
        )
    }
}

@Preview
@Composable
fun Preview() {
    SplashContent(
        state = SplashState(true),
        image = ImageBitmap.imageResource(R.drawable.img_pizza),
        onAnimationEnd = { }
    )
}
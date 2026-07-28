package com.example.zzzpicazzzas.presentation.screen.splash

import com.arttttt.nav3router.Router
import com.example.zzzpicazzzas.R
import com.example.zzzpicazzzas.domain.base.network.ResultValue
import com.example.zzzpicazzzas.domain.uscase.GetRemotePizzaListUseCase
import com.example.zzzpicazzzas.domain.uscase.SaveLocalPizzaListUseCase
import com.example.zzzpicazzzas.presentation.base.mvvm.BaseViewModel
import com.example.zzzpicazzzas.presentation.screen.Screen
import com.example.zzzpicazzzas.presentation.screen.splash.SplashLabel.NetworkExceptionMessage
import com.example.zzzpicazzzas.presentation.utils.toStringResourceWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getRemotePizzaListUseCase: GetRemotePizzaListUseCase,
    private val saveLocalPizzaListUseCase: SaveLocalPizzaListUseCase,
    private val router: Router<Screen>,
) : BaseViewModel<SplashState, SplashInternalState, SplashEvent, SplashLabel>(SplashState(), SplashInternalState()) {

    init {
        launchAnimation()
        loadPizzaList()

        observeInternalSate()
    }

    override fun onEvent(event: SplashEvent) = when (event) {
        SplashEvent.OnAnimationEnd -> updateInternalState { copy(isAnimationFinished = true) }
    }

    private fun launchAnimation() = launch {
        delay(ANIMATION_START_DELAY)
        updateState { copy(isAnimatePizza = true) }
    }

    private fun loadPizzaList() = launch {
        val pizzaListResult = getRemotePizzaListUseCase()

        when (pizzaListResult) {
            is ResultValue.Success -> {
                saveLocalPizzaListUseCase(pizzaListResult.data)

                updateInternalState { copy(isDataLoaded = true) }
            }
            is ResultValue.Error -> {
                val message = pizzaListResult.exception.message
                publishLabel(NetworkExceptionMessage(message.toStringResourceWrapper(R.string.network_error)))
            }
        }
    }

    private fun observeInternalSate() = launch {
        internalStateFlow.onEach(::checkLoadingAndAnimation).collect()
    }

    fun checkLoadingAndAnimation(internalState: SplashInternalState) {
        if (internalState.isAnimationFinished && internalState.isDataLoaded) {
            router.replaceCurrent(Screen.DetailScreen)
        }
    }

    companion object {

        private const val ANIMATION_START_DELAY = 100L
    }
}

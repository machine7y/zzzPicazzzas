package com.example.zzzpicazzzas.presentation.screen.splash

import com.example.zzzpicazzzas.presentation.base.mvvm.BaseEvent

sealed class SplashEvent : BaseEvent {

    data object OnAnimationEnd: SplashEvent()
}

package com.example.zzzpicazzzas.presentation.screen.detail

import com.example.zzzpicazzzas.presentation.base.mvvm.BaseLabel

sealed interface DetailLabel : BaseLabel {

    data object InternalLoadingException : DetailLabel
}

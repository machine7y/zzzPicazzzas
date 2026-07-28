package com.example.zzzpicazzzas.domain.base.network

sealed class ResultValue<out T> {

    data class Success<T>(val data: T) : ResultValue<T>()

    data class Error(val exception: Throwable) : ResultValue<Nothing>()
}

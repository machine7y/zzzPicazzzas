package com.example.zzzpicazzzas.presentation.utils

import android.content.Context
import androidx.annotation.StringRes

sealed interface StringResourceWrapper {

    data class StringWrapper(
        val value: String,
    ): StringResourceWrapper

    data class ResourceWrapper(
        @field:StringRes val id: Int,
    ): StringResourceWrapper
}

fun StringResourceWrapper.asString(context: Context): String = when (this) {
    is StringResourceWrapper.ResourceWrapper -> context.getString(id)
    is StringResourceWrapper.StringWrapper -> value
}

fun String?.toStringResourceWrapper(@StringRes stringId: Int): StringResourceWrapper = if (this == null) {
    StringResourceWrapper.ResourceWrapper(stringId)
} else {
    StringResourceWrapper.StringWrapper(this)
}

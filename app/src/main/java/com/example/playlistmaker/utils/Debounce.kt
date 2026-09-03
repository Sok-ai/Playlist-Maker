package com.example.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    scope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param ->
        if (useLastParam) debounceJob?.cancel()
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = scope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}
package com.jonolds.bugreportproject.ui.components.customlazylayout

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

@Composable
fun rememberLazyLayoutState(): LazyLayoutState {
    return remember { LazyLayoutState() }
}


@SuppressLint("AutoboxingStateCreation")
@Stable
class LazyLayoutState {

    private val _offsetState = mutableStateOf(0)
    val offsetState = _offsetState

    fun onDrag(offset: IntOffset) {
        val y = (_offsetState.value - offset.y).coerceAtLeast(0)
        _offsetState.value = y
    }

    fun getBoundaries(
        constraints: Constraints,
        threshold: Int = 500
    ) = IntRect(
        top = offsetState.value - threshold,
        bottom = constraints.maxHeight + offsetState.value+ threshold
    )
}

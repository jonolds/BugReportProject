package com.jonolds.bugreportproject.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager


inline fun <reified T> Modifier.thenIfNotNull(value: T?, modifier: Modifier.(T) -> Modifier) =
	if (value == null) this else modifier(value)



inline fun Modifier.thenIf(predicate: Boolean, modifier: Modifier.() -> Modifier) =
	if (predicate) modifier() else this


fun FocusManager.clearFocusOnClick(onClick: () -> Unit) {
	clearFocus()
	onClick()
}


fun Modifier.clearFocusOnClick(): Modifier = composed {
	val focus = LocalFocusManager.current
	this.clickable(
		interactionSource = remember{MutableInteractionSource()},
		indication = null,
		onClick = { focus.clearFocus() }
	)
}

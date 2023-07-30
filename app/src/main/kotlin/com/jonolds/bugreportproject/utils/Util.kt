@file:UseSerializers(
	SnapshotStateListSerializer::class, MutableStateSerializer::class,
	StateSerializer::class,
	SnapshotStateListSerializerEmptyIfNull::class
)
package com.jonolds.bugreportproject.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.utils.serializers.MutableStateSerializer
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializer
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializerEmptyIfNull
import com.jonolds.bugreportproject.utils.serializers.StateSerializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json


fun <T>MutableList<T>.move(from: Int, to: Int) {
	
	if(from != to)
		add(minOf(to, size-1), removeAt(from))
}


@Composable
fun SpacerBox(
	color: Color,
	height: Dp,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.padding(8.dp)
			.background(color)
			.fillMaxWidth()
			.requiredHeight(height)
	)
}





@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }
	var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
	if (isFocused) {
		val imeIsVisible = WindowInsets.isImeVisible
		val focusManager = LocalFocusManager.current
		LaunchedEffect(imeIsVisible) {
			if (imeIsVisible) {
				keyboardAppearedSinceLastFocused = true
			} else if (keyboardAppearedSinceLastFocused) {
				focusManager.clearFocus()
			}
		}
	}
	onFocusEvent {
		if (isFocused != it.isFocused) {
			isFocused = it.isFocused
			if (isFocused) {
				keyboardAppearedSinceLastFocused = false
			}
		}
	}
}



val json = Json {
	ignoreUnknownKeys = true
	isLenient = true
	encodeDefaults = true


}



fun String.trimNullIfBlank(): String? =
	trim().ifBlank { null }

fun String?.trimNullIfBlankCompare(other: String?): Boolean =
	this?.trim()?.ifBlank { null } == other?.trim()?.ifBlank { null }


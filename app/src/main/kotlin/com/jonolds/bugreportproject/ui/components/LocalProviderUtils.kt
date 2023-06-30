package com.jonolds.bugreportproject.ui.components

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

object NoRippleTheme : RippleTheme {
	@Composable
	override fun defaultColor() = Color.Unspecified

	@Composable
	override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
}


@Composable @OptIn(ExperimentalMaterial3Api::class)
fun NoRippleNoMinTouchSize(content: @Composable () -> Unit) {
	CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme, LocalMinimumInteractiveComponentEnforcement provides false) {
		content()
	}
}
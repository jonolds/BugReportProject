package com.jonolds.bugreportproject.ui.components.customlazylayout

import androidx.compose.runtime.Composable

typealias ComposableItemFactory = @Composable (Int) -> Unit

data class LazyLayoutItemContent(
	val itemY: Int,
	val itemContent: ComposableItemFactory
)

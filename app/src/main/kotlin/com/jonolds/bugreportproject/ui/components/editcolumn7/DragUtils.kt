package com.jonolds.bugreportproject.ui.components.editcolumn7

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import kotlin.math.roundToInt


val LayoutCoordinates.height: Int get() = size.height

fun LayoutCoordinates.localYPosOf(sourceCoordinates: LayoutCoordinates, yRelativeToSource: Int): Int =
	localPositionOf(sourceCoordinates, Offset(0f, yRelativeToSource.toFloat())).y.roundToInt()


fun LayoutCoordinates.localYPosOf(sourceCoordinates: LayoutCoordinates, yRelativeToSource: Float): Int =
	localPositionOf(sourceCoordinates, Offset(0f, yRelativeToSource)).y.roundToInt()


fun LayoutCoordinates?.isScrollCoors(): Boolean = this?.let { coors ->
	coors.height > coors.boundsInParent().height
} ?: false


fun LayoutCoordinates.findParentScrollCoors(): LayoutCoordinates? {
	var coors: LayoutCoordinates? = this
	while (coors != null) {
		if (coors.isScrollCoors())
			return coors
		coors = coors.parentLayoutCoordinates
	}
	return null
}


fun sign(value: Int): Int = when {
	value < 0 -> -1
	value > 0 -> 1
	else -> 0
}

fun getCentersFromHeights(heights: List<Int>): List<Int> {
	var acc = 0
	return heights.map { h -> (acc+h/2).also { acc+=h } }
}


fun getPosFromHeights(heights: List<Int>): List<Int> {
	var acc = 0
	return heights.map { h -> (acc).also { acc+=h } }
}

@Stable
fun <T> derivedStructural(
	calculation: () -> T
): State<T> = derivedStateOf(structuralEqualityPolicy()) { calculation() }
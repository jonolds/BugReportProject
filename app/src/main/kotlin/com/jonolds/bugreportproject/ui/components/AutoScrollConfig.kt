package com.jonolds.bugreportproject.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.jonolds.bugreportproject.ui.components.editcolumn7.sign
import kotlin.math.abs

@Stable
data class AutoScrollConfig(
	val numSpeedClasses: Int = 4,
	val minSpeed: Int = 60,
	val maxSpeed: Int = 300
) {

	val speeds: IntArray = ((maxSpeed-minSpeed)/(numSpeedClasses-1)).let { incrLength ->
		IntArray(numSpeedClasses) { speedClass ->
			if (speedClass == numSpeedClasses-1) return@IntArray maxSpeed
			minSpeed + speedClass*incrLength
		}
	}


	private val splitSize = 100/numSpeedClasses
	fun calculateSpeedClass(pct: Int): Int =
		(abs(pct)/splitSize).coerceIn(0 until numSpeedClasses)


	fun calculateSpeed(pct: Int): Int = sign(pct)*speeds[calculateSpeedClass(pct)]

}


@Composable
fun rememberAutoScrollConfig(
	numSpeedClasses: Int = 4,
	minSpeed: Int = 60,
	maxSpeed: Int = 300
): AutoScrollConfig = remember {
	AutoScrollConfig(numSpeedClasses, minSpeed, maxSpeed)
}

@Stable
fun calculateExcessItemPct(
	itemPosViewPort: Int,
	itemLen: Int,
	viewPortLen: Int,
): Int {

//	println("$itemPos $itemLen $windowLen")
	val excessValue = when {
		itemPosViewPort <= 0 -> itemPosViewPort
		itemPosViewPort + itemLen >= viewPortLen -> itemPosViewPort + itemLen - viewPortLen
		else -> 0
	}
	val excessPct = 100*excessValue/itemLen

	return excessPct.coerceIn(-100, 100)
}
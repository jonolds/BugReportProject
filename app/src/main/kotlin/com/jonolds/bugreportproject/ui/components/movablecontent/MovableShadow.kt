package com.jonolds.bugreportproject.ui.components.movablecontent

import android.annotation.SuppressLint
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.jonolds.bugreportproject.ui.components.calculateExcessItemPct


@SuppressLint("AutoboxingStateCreation")
@Stable
class MovableShadow(
	val targetIdx: Int,
	val itemLen: Int,
	val windowLen: Int,
	initialPosViewPort: Int,
) {

	private val _posViewPort = mutableStateOf(initialPosViewPort)

	val posViewPort by _posViewPort


	private val _excess = mutableStateOf(calculateExcessItemPct(posViewPort, itemLen, windowLen))
	val excess: Int by _excess

	private val _hasBeenFullyVisible = mutableStateOf(excess == 0)
	val hasBeenFullyVisible by _hasBeenFullyVisible


	@Stable
	fun update(newItemPos: Int) {
		_posViewPort.value = newItemPos
		_excess.value = calculateExcessItemPct(itemPosViewPort = posViewPort, itemLen = itemLen, viewPortLen = windowLen)
		_hasBeenFullyVisible.value = if (hasBeenFullyVisible) true else excess == 0

	}

}
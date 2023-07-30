package com.jonolds.bugreportproject.ui.components.editcolumn7

import android.annotation.SuppressLint
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.structuralEqualityPolicy
import com.jonolds.bugreportproject.ui.components.calculateExcessItemPct
import kotlin.math.roundToInt


@SuppressLint("AutoboxingStateCreation")
@Stable
data class ActiveDrag7(
	val targetIdx: Int,
	val itemLen: Int,
	val viewPortLen: Int,
	val initPosList: Int,
	val initPosViewPort: Int,
	val initScrollValue: Int,
	val firstAutoScrollValue: Int,
	val lastAutoScrollValue: Int,
	val getScrollValue: () -> Int?
) {


	private val _touchDelta = mutableStateOf(0f, structuralEqualityPolicy())
	val touchDelta by _touchDelta


	private val _posViewPort = mutableStateOf(initPosViewPort, structuralEqualityPolicy())
	val posViewPort by _posViewPort


	private val _excess = mutableStateOf(calculateExcessItemPct(posViewPort, itemLen, viewPortLen), structuralEqualityPolicy())
	val excess by _excess




	fun update2(dragAmount: Float) {
		_touchDelta.value += dragAmount
		_posViewPort.value = (initPosViewPort+touchDelta).roundToInt()
		_excess.value = calculateExcessItemPct(posViewPort, itemLen, viewPortLen)
	}


	val totalDelta by derivedStructural { (touchDelta+(getScrollValue() ?: 0)-initScrollValue).roundToInt() }

	val posList by derivedStructural { initPosList + totalDelta }

	val canAutoScrollForward by derivedStructural { (getScrollValue() ?: Int.MAX_VALUE) < lastAutoScrollValue }
	val canAutoScrollBackward by derivedStructural { (getScrollValue() ?: Int.MIN_VALUE) > firstAutoScrollValue }

}
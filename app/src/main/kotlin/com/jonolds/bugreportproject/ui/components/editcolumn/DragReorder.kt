package com.jonolds.bugreportproject.ui.components.editcolumn

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset


fun Modifier.dragReorder(state: DragState2, key: Any): Modifier = composed {

	val offset by remember(key) { derivedStateOf { state.getOffset(key) } }
	
	pointerInput(key) {
		
		detectDragGesturesAfterLongPress(
			onDragStart = { _ ->
				state.onDragStart(key)
			},
			onDrag = { change, dragAmount ->
				change.consume()
				state.onDrag(dragAmount) },
			onDragCancel = {
				state.onDragCancel()
		   	},
			onDragEnd = {
				state.onDragEnd()
			}
		)
	}
		.offset { IntOffset(0, offset) }
}


@SuppressLint("AutoboxingStateCreation")
@Stable
class DragState2(
	val move: (from: Int, to: Int) -> Unit,
	val scroll: ScrollState? = null,
	getKeys: () -> List<Any>
) {
	
	val keys: List<Any> by derivedStateOf { getKeys() }
	
	var heights by mutableStateOf(listOf<Int>())
	
	var startData by mutableStateOf<DragStartData?>(null)
	
	var touchDelta by mutableStateOf(0)
	
	
	
	val idxMap by derivedStateOf { keys.mapIndexed { i, k -> k to i }.toMap() }
	
	val scrollDelta by derivedStateOf {
		scroll?.value?.let { current ->
			startData?.startScrollPos?.let { start ->
				current-start
			} } ?: 0
	}
	
	val targetOffset by derivedStateOf { touchDelta + scrollDelta }
	
	val targetIdx by derivedStateOf { startData?.targetIdx }
	
	
	fun getOffset(key: Any): Int = derivedStateOf {
		val idx = idxMap[key] ?: return@derivedStateOf 0
		val offsets = offsets ?: return@derivedStateOf 0
		offsets.getOrElse(idx) { 0 }
	}.value
	
	
	val centers by derivedStateOf {
		if (heights.size != keys.size) null
		else getCentersFromHeights(heights)
	}
	
	
	
	val offsets by derivedStateOf { computeAllOffsets() }
	
	
	
	fun onDragStart(key: Any) {
		val idx = idxMap[key]!!
		
		touchDelta = 0
		startData = DragStartData(
			key = key,
			targetIdx = idx,
			startScrollPos = scroll?.value
		)
	}
	
	fun onDrag(dragAmount: Offset) {
		touchDelta += dragAmount.y.toInt()
	}
	
	fun onDragCancel() {
		startData = null
		touchDelta = 0

//		println("onDragCancel")
//		println("2 currentThread")
	
	}
	
	fun onDragEnd() {
		
		val data = startData!!
		
		val destIdx =
			if (targetOffset > 0)
				offsets!!.indexOfLast { it != 0 }
			else if (targetOffset < 0)
				offsets!!.indexOfFirst { it != 0 }
			else data.targetIdx
		
		
		if (destIdx != data.targetIdx && destIdx != -1)
			move(data.targetIdx, destIdx)
		
		
		onDragCancel()
	}
	
	
	
	
	fun computeAllOffsets(): IntArray? {
		
		val centers = centers ?: return null
		
		val targetIdx = startData?.targetIdx ?: return null
		
		return computeAllOffsets(targetIdx, targetOffset, heights, centers)
	}
	
	
}


internal fun computeAllOffsets(targetIdx: Int, targetOffset: Int, heights: List<Int>, centers: List<Int>? = null): IntArray {
	
	val centers2 = centers ?: getCentersFromHeights(heights)
	
	val newOffsets = IntArray(centers2.size)
	newOffsets[targetIdx] = targetOffset
	
	
	val targetEdge = targetOffset + centers2[targetIdx] +
		if (targetOffset < 0) -heights[targetIdx]/2 else heights[targetIdx]/2
	
	
	if (targetOffset < 0) {
		for (i in newOffsets.indices) {
			if (i == targetIdx) continue
			newOffsets[i] = if (i > targetIdx || centers2[i] < targetEdge) 0 else heights[targetIdx]
		}
		
	}
	else {
		for (i in newOffsets.indices) {
			if (i == targetIdx) continue
			newOffsets[i] = if (i < targetIdx || centers2[i] > targetEdge) 0 else -heights[targetIdx]
		}
		
	}
	
	return newOffsets
	
}

internal fun getCentersFromHeights(heights: List<Int>): List<Int> {
	var acc = 0
	return heights.map { h-> (acc+h/2).also { acc+=h } }
}

@Stable
data class DragStartData(
	val key: Any,
	val targetIdx: Int,
	val startScrollPos: Int? = null,
)





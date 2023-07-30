package com.jonolds.bugreportproject.ui.components.editcolumn7

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.IntOffset
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.DragStateInterface
import com.jonolds.bugreportproject.ui.components.LocalDragItemData2
import com.jonolds.bugreportproject.utils.thenIf
import kotlin.math.abs
import kotlin.math.roundToInt


@SuppressLint("AutoboxingStateCreation")
@Stable
class DragState7(
	val move: (from: Int, to: Int) -> Unit,
	val scroll: ScrollState? = null,
	config: AutoScrollConfig? = null,
	val getListCoors: () -> LayoutCoordinates,
): DragStateInterface {


	val listCoors by derivedStateOf { getListCoors() }
	val scrollCoors by derivedStateOf { listCoors.findParentScrollCoors() }
	val windowCoors by derivedStateOf {
		scrollCoors?.parentLayoutCoordinates
			?: listCoors.parentLayoutCoordinates
			?: listCoors
	}

	var heights by mutableStateOf(List(0) { 0 })

	val centers by derivedStateOf { getCentersFromHeights(heights) }

	var active by mutableStateOf<ActiveDrag7?>(null)

	override val targetIdx by derivedStateOf { active?.targetIdx }


	override fun getOffset(idx: Int): Int = active?.run {
		when {
			idx == targetIdx || totalDelta == 0 -> totalDelta
			sign(idx-targetIdx) != sign(totalDelta) -> 0
			totalDelta < 0 && posList < centers[idx] -> itemLen
			totalDelta > 0 && posList+itemLen > centers[idx] -> -itemLen
			else -> 0
		}
	} ?: 0


	override fun onDragStart(targetIdx: Int) {

		val itemLen = heights[targetIdx]

		val initPosList = centers[targetIdx] - itemLen/2

		val initPosWindow = windowCoors.localYPosOf(listCoors, initPosList)

		val viewPortBounds = windowCoors.localBoundingBoxOf(scrollCoors ?: listCoors)

		val windowLen = viewPortBounds.height.roundToInt()


		val listPosInScroll = scrollCoors?.localYPosOf(listCoors, 0) ?: 0
		val firstAutoScrollValue = listPosInScroll.minus(10).coerceAtLeast(0)
		val lastAutoScrollValue = listPosInScroll.plus(listCoors.height).minus(windowLen).plus(10)
			.coerceAtMost(scroll?.maxValue ?: windowLen)


		active = ActiveDrag7(
			targetIdx = targetIdx,
			itemLen = itemLen,
			viewPortLen = windowLen,
			initPosList = initPosList,
			initPosViewPort = initPosWindow-viewPortBounds.top.roundToInt(),
			firstAutoScrollValue = firstAutoScrollValue,
			lastAutoScrollValue = lastAutoScrollValue,
			getScrollValue = { scroll?.value ?: 0 },
			initScrollValue = scroll?.value ?: 0,
		)

	}

	override fun onDrag(dragAmount: Float) {
		active?.update2(dragAmount)

	}

	override fun onDragCancel() {
		active = null
	}

	override fun onDragEnd() {

		val active2 = active ?: return

		val destIdx2 = active2.run { when(sign(posList - initPosList)) {
			-1 -> centers.indexOfFirst { it > posList }
			1 -> centers.indexOfLast { it < posList + itemLen }
			else -> targetIdx
		} }

		if (destIdx2 != active2.targetIdx && destIdx2 != -1) {
			move(active2.targetIdx, destIdx2)
		}

		this.active = null
	}


	val speed by derivedStructural {
		val shadow = active ?: return@derivedStructural null

		val excess = shadow.excess
		if (
			scroll == null ||
			excess == 0 ||
			(excess < 0 && !shadow.canAutoScrollBackward) ||
			(excess > 0 && !shadow.canAutoScrollForward)
		)
			null
		else
			config?.calculateSpeed(excess)
	}

	val destDuration by derivedStructural {

		val speed = speed ?: return@derivedStructural null
		val active = active ?: return@derivedStructural null

		val destValue =
			if (speed < 0) active.firstAutoScrollValue
			else active.lastAutoScrollValue

		val duration = abs(100*(destValue - scroll!!.value)/speed)

		return@derivedStructural destValue to duration
	}

}


fun Modifier.dragItemContainer7(): Modifier = composed {

	val (key, getIdxFromKey, state, useHandleMod) = LocalDragItemData2.current?.let { remember(it) { it } } ?: let {
		System.err.println("LocalDragState not provided.")
		return@composed this
	}

	val idx by remember(key) { derivedStateOf { getIdxFromKey(key) }}

	val offset by remember(key) { derivedStructural { state.getOffset(idx) } }

	offset { IntOffset(0, offset) }
		.graphicsLayer {
			if (state.targetIdx == idx)
				alpha = 0f
		}
		.thenIf(!useHandleMod) {
			pointerInput(idx, state) {
				detectDragGesturesAfterLongPress(
					onDragStart = { _ -> state.onDragStart(idx) },
					onDrag = { _, dragAmount -> state.onDrag(dragAmount.y) },
					onDragCancel = state::onDragCancel,
					onDragEnd = state::onDragEnd
				)
			}
		}
}

fun Modifier.dragItemHandle7(): Modifier = composed {

	val (key, getIdxFromKey, state, useHandleMod) = LocalDragItemData2.current?.let { remember(it) { it } } ?: let {
//		System.err.println("LocalDragState not provided.")
		return@composed this
	}

	val idx by remember(key) { derivedStateOf { getIdxFromKey(key) }}



	thenIf(useHandleMod) {
		pointerInput(idx, state) {
			detectVerticalDragGestures(
				onDragStart = { _ -> state.onDragStart(idx) },
				onVerticalDrag = { _, dragAmount -> state.onDrag(dragAmount) },
				onDragCancel = state::onDragCancel,
				onDragEnd = state::onDragEnd
			)
		}
	}

}
package com.jonolds.bugreportproject.ui.components.reocolumn

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.DragStateInterface
import com.jonolds.bugreportproject.ui.components.LocalDragItemData2
import com.jonolds.bugreportproject.utils.thenIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@SuppressLint("AutoboxingStateCreation")
@Stable
class ReoState(
	val lazyState: LazyListState = LazyListState(),
	val autoScrollConfig: AutoScrollConfig = AutoScrollConfig(),
	scope: CoroutineScope
): CoroutineScope by scope, DragStateInterface {

	init {
		println("new state ${System.nanoTime()}")
	}

	lateinit var move: (from: Int, to: Int) -> Unit


	val layoutInfo by derivedStateOf { lazyState.layoutInfo }

	var shadow by mutableStateOf<ReoShadow?>(null)

	override val targetIdx: Int? get() = shadow?.targetIdx


	var currentWindowPos by mutableStateOf(0f)



	override fun onDragStart(targetIdx: Int) {

		val item = layoutInfo.visibleItemsInfo
			.firstOrNull { it.index == targetIdx}
			?: return

		if (item.contentType != ReoContentType.LIST_ITEM) {
			return
		}

		currentWindowPos = item.offset.toFloat()

		shadow = ReoShadow(
			targetIdx = item.index,
			initialPosViewPort = currentWindowPos.roundToInt(),
			itemLen = item.size,
			windowLen = layoutInfo.viewportSize.height,
		)

	}

	override fun onDrag(dragAmount: Float) {

		shadow?.let { shadow ->

			currentWindowPos+=dragAmount

			shadow.update(currentWindowPos.roundToInt())
		}
	}

	override fun onDragCancel() {
		shadow = null
	}

	override fun onDragEnd() {

		val shadow = shadow ?: return

		val targetIdx = shadow.targetIdx

		val visibleItems = layoutInfo.visibleItemsInfo


		val dest = visibleItems
			.filter { it.contentType == ReoContentType.LIST_ITEM && getOffset(it.index) != 0 }
			.maxByOrNull { kotlin.math.abs(targetIdx-it.index) }
			?.index ?: targetIdx

		if (dest != targetIdx)
			launch {
				move(targetIdx, dest)

				val lazyState = lazyState
				val firstVisIdx = visibleItems.first().index

				if (dest == firstVisIdx || targetIdx == firstVisIdx)
					lazyState.scrollToItem(lazyState.firstVisibleItemIndex, lazyState.firstVisibleItemScrollOffset)
				else if (targetIdx < firstVisIdx) {
					lazyState.scrollBy(shadow.itemLen.toFloat())
				}
			}

		this.shadow = null
	}

	override fun getOffset(idx: Int): Int {
		val shadow = shadow ?: return 0
		if (idx == shadow.targetIdx) return 0


		val visibleItems = layoutInfo.visibleItemsInfo
		val firstIdx = visibleItems.firstOrNull()?.index ?: return 0
		val lastIdx = visibleItems.lastOrNull()?.index ?: return 0

		if (idx !in firstIdx..lastIdx)
			return 0

		val info = visibleItems[idx-firstIdx]


		if (idx < shadow.targetIdx && shadow.posViewPort < info.offset+info.size/2)
			return shadow.itemLen

		if (idx > shadow.targetIdx && shadow.posViewPort + shadow.itemLen > info.offset+info.size/2)
			return -shadow.itemLen

		return 0
	}


}



@SuppressLint("AutoboxingStateCreation")
@Stable
fun Modifier.lazyReoGroup(state: ReoState): Modifier = composed {

//	val scope = rememberCoroutineScope()
//	pointerInput(state) {
//
//
//	}


	pointerInput(state) {
		detectDragGesturesAfterLongPress(
			onDragStart = { offset ->

				val item = state.layoutInfo.visibleItemsInfo
					.firstOrNull { it.offset + it.size >= offset.y}
					?: return@detectDragGesturesAfterLongPress

				state.onDragStart(item.index)

			},
			onDrag = { _, dragAmount ->
				state.onDrag(dragAmount.y)
			},
			onDragCancel = {
//				println("onDragCancel")
				state.onDragCancel()
			},
			onDragEnd = {
//				println("onDragEnd")
				state.onDragEnd()
			}
		)
	}
}


@SuppressLint("AutoboxingStateCreation")
@Stable
fun Modifier.reoHandle(): Modifier = composed {

	val (key, getIdxFromKey, state, useHandleMod) = LocalDragItemData2.current?.let { remember(it) { it } } ?: let {
//		System.err.println("LocalDragState not provided.")
		return@composed this
	}

	val idx by remember(key) { derivedStateOf { getIdxFromKey(key) }}

//	println(idx)


	thenIf(useHandleMod) {
		pointerInput(key, state) {
			detectVerticalDragGesturesChild(
				onDragStart = { _ ->
					println("onDragStart Child")
					state.onDragStart(idx) },
				onVerticalDrag = { _, dragAmount ->
					println("onVerticalDrag Child")
					state.onDrag(dragAmount)
			 	},
				onDragCancel = {
//					println("onDragCancel")
					state.onDragCancel()
				},
				onDragEnd = {
//					println("onDragEnd")
					state.onDragEnd()
				}
			)
		}
	}
}



@Stable
fun Modifier.reoItem(lazyDragState: ReoState, getIdx: () -> Int): Modifier = composed {

	val idx by remember {
		derivedStateOf { getIdx() }
	}

	val offset by remember(lazyDragState, idx) { derivedStateOf { lazyDragState.getOffset(idx) } }

	graphicsLayer {
		if (lazyDragState.shadow?.targetIdx == idx)
			alpha = 0f
	}
		.offset { IntOffset(0, offset) }
}


package com.jonolds.bugreportproject.ui.components.lazydragrow2

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.unit.IntOffset
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@SuppressLint("AutoboxingStateCreation")
@Stable
fun Modifier.lazyDrag2(state: LazyDragState2): Modifier = composed {

	var currentWindowPos by remember { mutableStateOf(0f) }

	pointerInput(state) {

		detectDragGesturesAfterLongPress(
			onDragStart = { offset ->

				val item = state.layoutInfo.visibleItemsInfo
					.firstOrNull { it.offset + it.size >= offset.x}
					?: return@detectDragGesturesAfterLongPress

				if (item.contentType != LazyDragContentType2.LIST_ITEM) {
					return@detectDragGesturesAfterLongPress
				}

				currentWindowPos = item.offset.toFloat()

				state.shadow = DragShadow2(
					targetIdx = item.index,
					initialPosViewPort = currentWindowPos.roundToInt(),
					itemLen = item.size,
					windowLen = state.layoutInfo.viewportSize.width,
				)

			},
			onDrag = { _, dragAmount ->
				currentWindowPos+=dragAmount.x
				state.shadow?.update(currentWindowPos.roundToInt())
			},
			onDragCancel = {
				state.shadow = null
			},
			onDragEnd = {
				val shadow = state.shadow ?: return@detectDragGesturesAfterLongPress

				val targetIdx = shadow.targetIdx

				val visibleItems = state.layoutInfo.visibleItemsInfo


				val dest = visibleItems
					.filter { it.contentType == LazyDragContentType2.LIST_ITEM && state.getOffset(it.index).x != 0 }
					.maxByOrNull { kotlin.math.abs(targetIdx-it.index) }
					?.index ?: targetIdx

				if (dest != targetIdx)
					state.launch {
						state.move(targetIdx, dest)

						val lazyState = state.lazyState
						val firstVisIdx = visibleItems.first().index

						if (dest == firstVisIdx || targetIdx == firstVisIdx)
							lazyState.scrollToItem(lazyState.firstVisibleItemIndex, lazyState.firstVisibleItemScrollOffset)
						else if (targetIdx < firstVisIdx) {
							lazyState.scrollBy(shadow.itemLen.toFloat())
						}
					}

				state.shadow = null

			}
		)
	}
}


@Stable
fun Modifier.lazyDragItem2(lazyDragState: LazyDragState2, idx: Int): Modifier = composed {

	val offset by remember(lazyDragState, idx) { derivedStateOf { lazyDragState.getOffset(idx) } }

	graphicsLayer {
		if (lazyDragState.shadow?.targetIdx == idx)
			alpha = 0f
	}
		.offset { offset }
}

@Stable
class LazyDragState2(
	val lazyState: LazyListState = LazyListState(),
	val autoScrollConfig: AutoScrollConfig = AutoScrollConfig(),
	scope: CoroutineScope
): CoroutineScope by scope {


	lateinit var move: (from: Int, to: Int) -> Unit

	var reorderRange = IntRange.EMPTY

	val layoutInfo by derivedStateOf { lazyState.layoutInfo }

	var shadow by mutableStateOf<DragShadow2?>(null)

	fun getOffset(idx: Int): IntOffset {
		val shadow = shadow ?: return IntOffset.Zero
		if (idx == shadow.targetIdx) return IntOffset.Zero


		val visibleItems = layoutInfo.visibleItemsInfo
		val firstIdx = visibleItems.firstOrNull()?.index ?: return IntOffset.Zero
		val lastIdx = visibleItems.lastOrNull()?.index ?: return IntOffset.Zero

		if (idx !in firstIdx..lastIdx)
			return IntOffset.Zero

		val info = visibleItems[idx-firstIdx]


		if (idx < shadow.targetIdx && shadow.posViewPort < info.offset+info.size/2)
			return IntOffset(shadow.itemLen, 0)

		if (idx > shadow.targetIdx && shadow.posViewPort + shadow.itemLen > info.offset+info.size/2)
			return IntOffset(-shadow.itemLen, 0)

		return IntOffset.Zero
	}


}
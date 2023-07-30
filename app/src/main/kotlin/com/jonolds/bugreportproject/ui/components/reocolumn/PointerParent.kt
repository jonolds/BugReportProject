package com.jonolds.bugreportproject.ui.components.reocolumn

import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange


suspend fun PointerInputScope.detectVerticalDragGesturesParent(
	onDragStart: (Offset) -> Unit = { },
	onDragEnd: () -> Unit = { },
	onDragCancel: () -> Unit = { },
	onVerticalDrag: (change: PointerInputChange, dragAmount: Float) -> Unit
) {
	awaitEachGesture {
		val down = awaitFirstDown(requireUnconsumed = false)
		val drag = awaitDragOrCancellation(down.id)
		println("got drag2=$drag")
		var overSlop = 0f
//		val drag = awaitVerticalPointerSlopOrCancellation(down.id, down.type) { change, over ->
//			change.consume()
//			overSlop = over
//		}
		if (drag != null) {
			drag.consume()
//			onDragStart.invoke(drag.position)
			onVerticalDrag.invoke(drag, overSlop)
			if (
				verticalDrag(drag.id) {
					onVerticalDrag(it, it.positionChange().y)
//					it.consume()
				}
			) {
				onDragEnd()
			} else {
				onDragCancel()
			}
		}
	}
}
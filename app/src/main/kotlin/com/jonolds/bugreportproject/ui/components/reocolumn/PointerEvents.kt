package com.jonolds.bugreportproject.ui.components.reocolumn

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import kotlin.math.abs
import kotlin.math.sign

suspend fun PointerInputScope.detectVerticalDragGestures2(
	onDragStart: (Offset) -> Unit = { },
	onDragEnd: () -> Unit = { },
	onDragCancel: () -> Unit = { },
	onVerticalDrag: (change: PointerInputChange, dragAmount: Float) -> Unit
) {
	awaitEachGesture {
		val down = awaitFirstDown(requireUnconsumed = false)
		var overSlop = 0f
		val drag = awaitVerticalPointerSlopOrCancellation(down.id, down.type) { change, over ->
			change.consume()
			overSlop = over
		}
		if (drag != null) {
			onDragStart.invoke(drag.position)
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

suspend fun AwaitPointerEventScope.awaitVerticalPointerSlopOrCancellation(
	pointerId: PointerId,
	pointerType: PointerType,
	onTouchSlopReached: (change: PointerInputChange, overSlop: Float) -> Unit
) = awaitPointerSlopOrCancellation(
	pointerId = pointerId,
	pointerType = pointerType,
	onPointerSlopReached = { change, overSlop -> onTouchSlopReached(change, overSlop.y) },
	pointerDirectionConfig = VerticalPointerDirectionConfig
)


suspend inline fun AwaitPointerEventScope.awaitPointerSlopOrCancellation(
	pointerId: PointerId,
	pointerType: PointerType,
	pointerDirectionConfig: PointerDirectionConfig = HorizontalPointerDirectionConfig,
	triggerOnMainAxisSlop: Boolean = true,
	onPointerSlopReached: (PointerInputChange, Offset) -> Unit,
): PointerInputChange? {
	if (currentEvent.isPointerUp(pointerId)) {
		return null // The pointer has already been lifted, so the gesture is canceled
	}
	val touchSlop = viewConfiguration.pointerSlop(pointerType)
	var pointer: PointerId = pointerId
	var totalMainPositionChange = 0f
	var totalCrossPositionChange = 0f

	while (true) {
		val event = awaitPointerEvent()

		val dragEvent = event.changes.fastFirstOrNull { it.id == pointer } ?: return null
		if (dragEvent.isConsumed) {
			return null
		} else if (dragEvent.changedToUpIgnoreConsumed()) {
			val otherDown = event.changes.fastFirstOrNull { it.pressed }
			if (otherDown == null) {
				// This is the last "up"
				return null
			} else {
				pointer = otherDown.id
			}
		} else {
			val currentPosition = dragEvent.position
			val previousPosition = dragEvent.previousPosition

			val mainPositionChange = pointerDirectionConfig.mainAxisDelta(currentPosition) -
				pointerDirectionConfig.mainAxisDelta(previousPosition)

			val crossPositionChange = pointerDirectionConfig.crossAxisDelta(currentPosition) -
				pointerDirectionConfig.crossAxisDelta(previousPosition)
			totalMainPositionChange += mainPositionChange
			totalCrossPositionChange += crossPositionChange

			val inDirection = if (triggerOnMainAxisSlop) {
				abs(totalMainPositionChange)
			} else {
				pointerDirectionConfig.offsetFromChanges(
					totalMainPositionChange,
					totalCrossPositionChange
				).getDistance()
			}
			if (inDirection < touchSlop) {
				// verify that nothing else consumed the drag event
				awaitPointerEvent(PointerEventPass.Final)
				if (dragEvent.isConsumed) {
					return null
				}
			} else {
				val postSlopOffset = if (triggerOnMainAxisSlop) {
					val finalMainPositionChange = totalMainPositionChange -
						(sign(totalMainPositionChange) * touchSlop)
					pointerDirectionConfig.offsetFromChanges(
						finalMainPositionChange,
						totalCrossPositionChange
					)
				} else {
					val offset = pointerDirectionConfig.offsetFromChanges(
						totalMainPositionChange,
						totalCrossPositionChange
					)
					val touchSlopOffset = offset / inDirection * touchSlop
					offset - touchSlopOffset
				}

				onPointerSlopReached(
					dragEvent,
					postSlopOffset
				)
				if (dragEvent.isConsumed) {
					return dragEvent
				} else {
					totalMainPositionChange = 0f
					totalCrossPositionChange = 0f
				}
			}
		}
	}
}

fun PointerEvent.isPointerUp(pointerId: PointerId): Boolean =
	changes.fastFirstOrNull { it.id == pointerId }?.pressed != true


fun ViewConfiguration.pointerSlop(pointerType: PointerType): Float {
	return when (pointerType) {
		PointerType.Mouse -> touchSlop * mouseToTouchSlopRatio
		else -> touchSlop
	}
}

private val mouseSlop = 0.125.dp
private val defaultTouchSlop = 18.dp // The default touch slop on Android devices
private val mouseToTouchSlopRatio = mouseSlop / defaultTouchSlop


interface PointerDirectionConfig {
	fun mainAxisDelta(offset: Offset): Float
	fun crossAxisDelta(offset: Offset): Float
	fun offsetFromChanges(mainChange: Float, crossChange: Float): Offset
}

/**
 * Used for monitoring changes on X axis.
 */
val HorizontalPointerDirectionConfig = object : PointerDirectionConfig {
	override fun mainAxisDelta(offset: Offset): Float = offset.x
	override fun crossAxisDelta(offset: Offset): Float = offset.y
	override fun offsetFromChanges(mainChange: Float, crossChange: Float): Offset =
		Offset(mainChange, crossChange)
}

/**
 * Used for monitoring changes on Y axis.
 */
val VerticalPointerDirectionConfig = object : PointerDirectionConfig {
	override fun mainAxisDelta(offset: Offset): Float = offset.y

	override fun crossAxisDelta(offset: Offset): Float = offset.x

	override fun offsetFromChanges(mainChange: Float, crossChange: Float): Offset =
		Offset(crossChange, mainChange)
}

fun Orientation.toPointerDirectionConfig(): PointerDirectionConfig =
	if (this == Orientation.Vertical) VerticalPointerDirectionConfig
	else HorizontalPointerDirectionConfig
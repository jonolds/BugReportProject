package com.jonolds.bugreportproject.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Stable @OptIn(ExperimentalFoundationApi::class)
class SwipeAwayState(
	val onDismiss: () -> Unit,
	pctThreshold: Float,
	reverseDirection: Boolean = false,
	private val targetAwayValue: AwayValue = AwayValue.DismissedToEnd,
	animationSpec: AnimationSpec<Float>
) {


	private var size: IntSize = IntSize.Zero

	val anchoredState = AnchoredDraggableState(
		initialValue = AwayValue.Default,
		anchors = computeAnchors(size),
		positionalThreshold = { totalDistance: Float -> (if (reverseDirection) -1 else 1)*totalDistance*pctThreshold },
		velocityThreshold = { 30000000f },
		animationSpec = animationSpec,
		confirmValueChange = {
			if (it == targetAwayValue)
				onDismiss()
			it == targetAwayValue
		}
	)


	val offset by derivedStateOf { if (reverseDirection) -anchoredState.offset else anchoredState.offset }

	fun computeAnchors(size: IntSize): DraggableAnchors<AwayValue> {
		return DraggableAnchors {
			when(targetAwayValue) {
				AwayValue.DismissedToEnd -> {
					AwayValue.Default at 0f
					AwayValue.DismissedToEnd at size.width.toFloat()
				}
				AwayValue.DismissedToStart -> {
					AwayValue.Default at 0f
					AwayValue.DismissedToStart at size.width.toFloat()
				}
				AwayValue.DismissedToBottom -> {
					AwayValue.Default at 0f
					AwayValue.DismissedToBottom at size.height.toFloat()
				}
				AwayValue.DismissedToTop -> {
					AwayValue.Default at 0f
					AwayValue.DismissedToTop at size.height.toFloat()
				}
				else -> {}
			}
		}

	}

	fun onSizeChanged(newSize: IntSize) {
		if (newSize == size)
			return
		size = newSize

		val newAnchors = computeAnchors(newSize)
		anchoredState.updateAnchors(newAnchors)
	}
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeAway(
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
	pctThreshold: Float = .35f,
	orientation: Orientation = Orientation.Horizontal,
	reverseDirection: Boolean = false,
	animationSpec: AnimationSpec<Float> = tween(durationMillis = 300, easing = LinearEasing),
	dismissContent: @Composable (RowScope.() -> Unit),
) {

	val state = remember {
		SwipeAwayState(
			pctThreshold = pctThreshold,
			animationSpec = animationSpec,
			onDismiss = onDismiss,
			reverseDirection = reverseDirection,
			targetAwayValue = computeTargetAwayValue(orientation, reverseDirection)
		)
	}

	Row(
		content = dismissContent,
		modifier = modifier
			.anchoredDraggable(
				state = state.anchoredState,
				orientation = orientation,
				reverseDirection = reverseDirection
			)
			.graphicsLayer {
				if (orientation == Orientation.Horizontal)
					translationX = state.offset
				else
					translationY = state.offset
				alpha = 1f - state.anchoredState.offset / (pctThreshold * state.anchoredState.anchors.maxAnchor())
			}
			.onSizeChanged { state.onSizeChanged(it) }
	)
}

@Stable
internal fun computeTargetAwayValue(
	orientation: Orientation,
	reverseDirection: Boolean
): AwayValue =
	when(orientation) {
		Orientation.Horizontal -> if (reverseDirection) AwayValue.DismissedToStart else AwayValue.DismissedToEnd
		else -> if (reverseDirection) AwayValue.DismissedToTop else AwayValue.DismissedToBottom
	}

@Stable
enum class AwayValue {
	Default, DismissedToEnd, DismissedToStart, DismissedToBottom, DismissedToTop
}
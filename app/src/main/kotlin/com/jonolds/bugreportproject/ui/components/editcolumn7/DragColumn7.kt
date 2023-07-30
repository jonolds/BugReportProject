package com.jonolds.bugreportproject.ui.components.editcolumn7

import android.util.ArraySet
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastMap
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.DragItemData2
import com.jonolds.bugreportproject.ui.components.LocalDragItemData2
import kotlin.math.abs


@Composable
fun <T> DragColumn7(
	elems: List<T>,
	toKey: (T) -> Any,
	move: (from: Int, to: Int) -> Unit,
	modifier: Modifier = Modifier,
	scrollState: ScrollState? = null,
	scrollConfig: AutoScrollConfig? = null,
	useHandleMod: Boolean = false,
	contentFactory: @Composable ((T) -> Unit)
) {

	var listCoors by remember { mutableStateOf<LayoutCoordinates>(EmptyLayoutCoordinates) }

	val state by remember { mutableStateOf(
		DragState7(
			move = move,
			scroll = scrollState,
			config = scrollConfig,
			getListCoors = { listCoors }
		)
	) }

	val keyIdxMap by remember(elems) { derivedStateOf { elems.mapIndexed { i, elem -> toKey(elem) to i }.toMutableStateMap() } }

	val movables = elems.getMovable(toKey = toKey) { elem ->
		ElemHolder(
			elem = elem,
			toKey = toKey,
			getIdxFromKey = { keyIdxMap[it] ?: 0 },
			state = state,
			useHandleMod = useHandleMod,
			contentFactory = contentFactory
		)
	}

	Box(
		modifier = modifier
			.onPlaced { _layoutCoordinates -> listCoors = _layoutCoordinates }
	) {


		Layout(
			content = {
				for (elem in elems)
					movables(elem)
			},
		) { measurables, constrs ->

			val placeables = measurables.fastMap { it.measure(constrs) }

			state.heights = placeables.fastMap { it.height }

			layout(constrs.maxWidth, placeables.sumOf { it.height }) {
				var y = 0
				for (pl in placeables) {
					pl.placeRelative(0, y)
					y+=pl.height
				}
			}

		}

		Shadow7(
			state = state,
			elems = elems,
			contentFactory = contentFactory
		)

	}

	ScrollEffect7(state = state)
}

@Composable
fun <T> Shadow7(
	state: DragState7,
	elems: List<T>,
	modifier: Modifier = Modifier,
	contentFactory: @Composable ((T) -> Unit)
) {

	val active = state.active ?: return



	val idx by remember { derivedStructural { active.targetIdx } }

	val offset by remember { derivedStructural { active.initPosList + state.getOffset(idx) } }


	Box(
		content = { contentFactory(elems[idx]) },
		modifier = modifier
			.offset { IntOffset(0, offset) }
	)

}


@Composable
fun ScrollEffect7(state: DragState7) {


	val scroll = state.scroll ?: return

	val active = state.active ?: return

	val speed = state.speed ?: return

	val destValue = remember(speed) {
		if (speed < 0) active.firstAutoScrollValue
		else active.lastAutoScrollValue
	}

	val duration = remember(speed) {
		abs(100*(destValue - scroll.value)/speed)
	}

	LaunchedEffect(destValue, duration) {
		scroll.animateScrollTo(
			value = destValue,
			animationSpec = tween(durationMillis = duration, easing = LinearEasing)
		)
	}


}


@Composable
fun <T> List<T>.getMovable(toKey: (T) -> Any, transform: @Composable (item: T) -> Unit): @Composable (item: T) -> Unit {
	val composedItems = remember(this) { mutableMapOf<Any, @Composable () -> Unit>() }
	return { item: T ->
		composedItems.getOrPut(toKey(item)) { movableContentOf { transform(item) } }.invoke()
	}
}


@Composable
fun <T>ElemHolder(
	elem: T,
	toKey: (T) -> Any,
	getIdxFromKey: (Any) -> Int,
	state: DragState7,
	useHandleMod: Boolean,
	contentFactory: @Composable (T) -> Unit
) {

	CompositionLocalProvider(LocalDragItemData2 provides DragItemData2(toKey(elem), getIdxFromKey, state, useHandleMod)) {

//		println("new box ${toKey(elem)}")
		Box(
			content = {
				contentFactory(elem) },
			modifier = Modifier
				.dragItemContainer7()
		)
	}
}

@Stable
internal object EmptyLayoutCoordinates : LayoutCoordinates {
	override val isAttached: Boolean = false
	override val parentCoordinates: LayoutCoordinates? = null
	override val parentLayoutCoordinates: LayoutCoordinates? = null
	override val providedAlignmentLines: Set<AlignmentLine> = ArraySet()
	override val size: IntSize = IntSize.Zero

	override fun get(alignmentLine: AlignmentLine): Int = AlignmentLine.Unspecified

	override fun localBoundingBoxOf(sourceCoordinates: LayoutCoordinates, clipBounds: Boolean): Rect = Rect.Zero

	override fun localPositionOf(sourceCoordinates: LayoutCoordinates, relativeToSource: Offset): Offset = Offset.Zero

	override fun localToRoot(relativeToLocal: Offset): Offset = Offset.Zero

	override fun localToWindow(relativeToLocal: Offset): Offset = Offset.Zero

	override fun windowToLocal(relativeToWindow: Offset): Offset = Offset.Zero

}
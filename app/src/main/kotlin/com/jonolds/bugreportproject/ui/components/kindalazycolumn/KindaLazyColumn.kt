package com.jonolds.bugreportproject.ui.components.kindalazycolumn

import android.util.ArraySet
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastSumBy
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.DragItemData2
import com.jonolds.bugreportproject.ui.components.LocalDragItemData2
import com.jonolds.bugreportproject.ui.components.editcolumn7.DragState7
import com.jonolds.bugreportproject.ui.components.editcolumn7.dragItemContainer7
import com.jonolds.bugreportproject.ui.components.editcolumn7.height
import com.jonolds.bugreportproject.ui.components.editcolumn7.localYPosOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun <T> KindaLazyColumn(
	elems: List<T>,
	toKey: (T) -> Any,
	move: (from: Int, to: Int) -> Unit,
	modifier: Modifier = Modifier,
	scrollState: ScrollState? = null,
	scrollConfig: AutoScrollConfig? = null,
	useHandleMod: Boolean = true,
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

	val itemProvider = remember(elems) {
		KindaLazyItemProvider(
			getKeys = { elems.map(toKey) },
			getScrollValue = { scrollState?.value ?: 0},
			getOriginInScroll = { state.scrollCoors?.localYPosOf(state.listCoors, 0) ?: 0},
			getViewPortHeight = { state.scrollCoors?.boundsInParent()?.height?.roundToInt() ?: state.listCoors.height },

		)
	}


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

	val numIdxsToLoad by itemProvider.numIdxsToLoad.collectAsState(initial = 0)



	Layout(
		content = {
			for (i in 0 until minOf(elems.size, numIdxsToLoad))
				movables(elems[i])
		},
		modifier = modifier
			.onPlaced { _layoutCoordinates -> listCoors = _layoutCoordinates }
	) { measurables, constrs ->

		val placeables = measurables.fastMap { it.measure(constrs) }

//		println("measured ${placeables.size} of ${elems.size}")
		val newHeights = placeables.fastMap { it.height }

		state.heights= newHeights

		val sum = newHeights.fastSumBy { it }
		val avg = if (newHeights.isEmpty()) 0 else sum/newHeights.size
		itemProvider.updateAvg(avg)

		val layoutHeight = sum + avg*(elems.size-measurables.size).coerceAtLeast(0)

		layout(constrs.maxWidth, layoutHeight) {
			var y = 0
			for (pl in placeables) {
				pl.placeRelative(0, y)
				y+=pl.height
			}
		}

	}



	val scope = rememberCoroutineScope()
	SideEffect {
		if (numIdxsToLoad < elems.size)
			scope.launch(Dispatchers.Default) {
				delay(100)
				itemProvider.incrementMaxScrollUserInput()
			}
	}

	ScrollEffect7(state = state)

}


@Composable
fun ScrollEffect7(state: DragState7) {


	val scroll = state.scroll ?: return

	val scope = rememberCoroutineScope()
	LaunchedEffect(Unit) {

		scope.launch(Dispatchers.IO) {
			snapshotFlow { state.destDuration }.collectLatest { destDuration ->
				val (destValue, duration) = destDuration ?: return@collectLatest

				scroll.animateScrollTo(
					value = destValue,
					animationSpec = tween(durationMillis = duration, easing = LinearEasing)
				)

			}

		}
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

//	println("new movable ${toKey(elem)}")
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

@Composable
fun <T> List<T>.getMovable(toKey: (T) -> Any, transform: @Composable (item: T) -> Unit): @Composable (item: T) -> Unit {
	val composedItems = remember(this) { mutableMapOf<Any, @Composable () -> Unit>() }
	return { item: T ->
		composedItems.getOrPut(toKey(item)) { movableContentOf { transform(item) } }.invoke()
	}
}
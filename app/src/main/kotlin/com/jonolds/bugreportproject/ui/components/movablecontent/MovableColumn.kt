package com.jonolds.bugreportproject.ui.components.movablecontent

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.DragItemData2
import com.jonolds.bugreportproject.ui.components.LocalDragItemData2
import com.jonolds.bugreportproject.ui.components.rememberAutoScrollConfig
import com.jonolds.bugreportproject.utils.thenIf
import kotlin.math.abs



@Composable
fun <T>MovableColumn(
	elems: List<T>,
	toKey: (T) -> Any,
	move: (Int, Int) ->  Unit,
	reorderContentFactory: @Composable (T) -> Unit,
	modifier: Modifier = Modifier,
	lazyState: LazyListState = rememberLazyListState(),
	autoScrollConfig: AutoScrollConfig = rememberAutoScrollConfig(),
	useHandleMod: Boolean = false,
	content: MovableScope<T>.() -> Unit,

) {

	println("new MovableColumn")

	val scope = rememberCoroutineScope()


	var beforeReorderCount by remember { mutableIntStateOf(0) }

	val state = remember {
		MovableState(
			lazyState = lazyState,
			autoScrollConfig = autoScrollConfig,
			scope = scope,
			move = { from, to -> move(from-beforeReorderCount, to-beforeReorderCount) }
	)}


	val keyIdxMap by remember(elems) { derivedStateOf { elems.mapIndexed { i, elem -> toKey(elem) to i+beforeReorderCount }.toMap() }}



	val dragScope = remember {
		MovableScope(
			contentMap2 = elems.map { elem -> toKey(elem) to movableContentOf {
				val dragItemData = remember(elem) {
					val key = toKey(elem)
					DragItemData2(key, { keyIdxMap[key]!! }, state, useHandleMod)
				}
				CompositionLocalProvider(LocalDragItemData2 provides dragItemData) {

					Box(
						content = { reorderContentFactory(elem) },
					)
				}
			}}.toMap(),
			setBeforeReorderCount = { beforeReorderCount = it },
			elems = elems,
			toKey = toKey,
			state = state,
			useHandleMod = useHandleMod,
		)
	}

	Box(
		modifier = modifier
			.clipToBounds()

	) {



		LazyColumn(
			state = state.lazyState,
			content = { dragScope.processContent(this, content) },
			modifier = Modifier
				.thenIf(!useHandleMod) { movableGroup(state) }
				.pointerInput(Unit) {

					detectVerticalDragGestures(
						onDragStart = { _ ->
							println("onDragStart Parent")
						},
						onVerticalDrag = { _, dragAmount ->
							println("onVerticalDrag Parent")
						},
						onDragCancel = {
						},
						onDragEnd = {
						}
					)
				}
		)


		DrawShadow(state = state, getContentForIdx = { idx -> reorderContentFactory(elems[idx-beforeReorderCount])})

	}

	LazyScrollEffectMovable(state = state)

}


@Composable
fun LazyScrollEffectMovable(state: MovableState) {

	val shadow = state.shadow ?: return

	val readyToScroll by remember(shadow) { derivedStateOf {
		shadow.excess != 0 &&
			shadow.hasBeenFullyVisible &&
			if (shadow.excess < 0) state.lazyState.canScrollBackward
				else state.lazyState.canScrollForward }}

	if (!readyToScroll)
		return


	val dest = remember(shadow.excess) { if (shadow.excess < 0) -1 else 1 }

	val dist by remember { derivedStateOf { state.layoutInfo.viewportSize.height }}

	val config = state.autoScrollConfig

	val speed = remember(shadow.excess) { config.calculateSpeed(shadow.excess) }

	val duration = remember(speed, dest) { abs(100*dist/speed) }

	LaunchedEffect(duration, dest, state.shadow) {

		while (state.shadow != null) {
//			println("while  ${state.shadow == null}")
			state.lazyState.animateScrollBy(
				value = (dest * dist).toFloat(),
				animationSpec = tween(durationMillis = duration, easing = LinearEasing)
			)
		}
	}



}


@Composable
fun DrawShadow(state: MovableState, getContentForIdx: @Composable (Int) -> Unit) {

	val shadow = state.shadow ?: return


	val targetIdx = shadow.targetIdx

	val itemPos by remember(shadow) { derivedStateOf { shadow.posViewPort } }

	Box(
		content = {
			getContentForIdx(targetIdx)
		},
		modifier = Modifier
			.offset { IntOffset(0, itemPos) }
	)

}



@Composable
fun <T> List<T>.movable(toKey: (T) -> Any, transform: @Composable LazyItemScope.(item: T) -> Unit): @Composable LazyItemScope.(item: T) -> Unit {
	val composedItemsMap = remember(this) { mutableMapOf<Any, @Composable LazyItemScope.() -> Unit>() }
	return { item: T ->


		val key = toKey(item)

		println("key=$key  keys = ${composedItemsMap.keys}")
		composedItemsMap.getOrPut(key) { movableContentWithReceiverOf<LazyItemScope>{ transform(item) } }
	}
}
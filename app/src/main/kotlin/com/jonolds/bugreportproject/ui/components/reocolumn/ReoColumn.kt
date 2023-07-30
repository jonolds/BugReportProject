package com.jonolds.bugreportproject.ui.components.reocolumn

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.rememberAutoScrollConfig
import com.jonolds.bugreportproject.utils.thenIf
import kotlin.math.abs


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReoColumn(
	modifier: Modifier = Modifier,
	lazyState: LazyListState = rememberLazyListState(),
	autoScrollConfig: AutoScrollConfig = rememberAutoScrollConfig(),
	useHandleMod: Boolean = false,
	content: ReoScope.() -> Unit,

) {
	val scope = rememberCoroutineScope()

	val state = remember {
		ReoState(
			lazyState = lazyState,
			autoScrollConfig = autoScrollConfig,
			scope = scope
	)}

	val dragScope = remember { ReoScope(state, useHandleMod) }



	Box(
		modifier = modifier
			.clipToBounds()

	) {


		LazyColumn(
			state = state.lazyState,
			content = {
		  		dragScope.processContent(this, content)
			},
			modifier = Modifier
				.thenIf(!useHandleMod) { lazyReoGroup(state) }
				.pointerInput(Unit) {

					detectVerticalDragGesturesParent(
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


		DrawShadow(state = state, dragScope = dragScope)

	}

	LazyScrollEffectReo(state = state)

}


@Composable
fun LazyScrollEffectReo(state: ReoState) {

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
fun DrawShadow(state: ReoState, dragScope: ReoScope) {

	val shadow = state.shadow ?: return


	val targetIdx = shadow.targetIdx

	val itemPos by remember(shadow) { derivedStateOf { shadow.posViewPort } }

	Box(
		content = {
			dragScope.shadowContentFactory(targetIdx)
		},
		modifier = Modifier
			.offset { IntOffset(0, itemPos) }
	)

}
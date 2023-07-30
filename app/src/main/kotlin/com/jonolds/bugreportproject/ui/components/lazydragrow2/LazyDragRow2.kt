package com.jonolds.bugreportproject.ui.components.lazydragrow2

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.IntOffset
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.rememberAutoScrollConfig
import kotlin.math.abs


@Composable
fun LazyDragRow2(
	modifier: Modifier = Modifier,
	lazyState: LazyListState = rememberLazyListState(),
	autoScrollConfig: AutoScrollConfig = rememberAutoScrollConfig(),
	content: LazyDragScope2.() -> Unit
) {

	val coScope = rememberCoroutineScope()
	val state = remember {
		LazyDragState2(
			lazyState = lazyState,
			autoScrollConfig = autoScrollConfig,
			scope = coScope
		)
	}



	Box(
		modifier = modifier
			.clipToBounds()
	) {

		val dragScope = remember(state) { LazyDragScope2(state) }
		LazyRow(
			state = state.lazyState,
			content = {
				dragScope.processContent(this, content)
			},
			modifier = Modifier
				.lazyDrag2(state)
		)

		DrawShadow(state = state, dragScope = dragScope)

	}

	LazyScrollEffect2(state = state)
}


@Composable
fun DrawShadow(state: LazyDragState2, dragScope: LazyDragScope2) {

	val shadow = state.shadow ?: return


	val targetIdx = remember { shadow.targetIdx }

	val itemPos by remember(shadow) { derivedStateOf { shadow.posViewPort } }

	Box(
		content = {
			dragScope.shadowContentFactory!!(targetIdx)
		},
		modifier = Modifier
			.offset { IntOffset(itemPos, 0) }
	)

}


@Composable
fun LazyScrollEffect2(state: LazyDragState2) {

	val shadow = state.shadow ?: return

	val readyToScroll = shadow.excess != 0 &&
		shadow.hasBeenFullyVisible &&
		if (shadow.excess < 0) state.lazyState.canScrollBackward
		else state.lazyState.canScrollForward

	if (!readyToScroll)
		return


	val dest = remember(shadow.excess) { if (shadow.excess < 0) -1 else 1 }

	val dist by remember { derivedStateOf { state.layoutInfo.viewportSize.width }}

	val config = state.autoScrollConfig

	val speed = remember(shadow.excess) { config.calculateSpeed(shadow.excess) }

	val duration = remember(speed) { abs(100*dist/speed) }

	LaunchedEffect(duration, dest) {

		while (true)
			state.lazyState.animateScrollBy(
				value = (dest*dist).toFloat(),
				animationSpec = tween(durationMillis = duration, easing = LinearEasing)
			)
	}



}
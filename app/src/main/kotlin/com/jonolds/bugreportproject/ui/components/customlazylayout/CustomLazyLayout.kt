package com.jonolds.bugreportproject.ui.components.customlazylayout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.fastForEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomLazyLayout(
    modifier: Modifier = Modifier,
    state: LazyLayoutState = rememberLazyLayoutState(),
    content: CustomLazyListScope.() -> Unit
) {
    val itemProvider = rememberItemProvider(content)

    LazyLayout(
        modifier = modifier
            .clipToBounds()
            .lazyLayoutPointerInput(state),
        itemProvider = itemProvider,
        measurePolicy = { constraints ->
            val boundaries = state.getBoundaries(constraints)
            val indexes = itemProvider.getItemIndexesInRange(boundaries)

            val indexesWithPlaceables = indexes.associateWith {
                measure(it, Constraints())
            }

            layout(constraints.maxWidth, constraints.maxHeight) {

                indexesWithPlaceables.forEach { (i, placeables) ->
                    val itemY = itemProvider.getItem(i)
                    itemY?.let { placeItem(state, itemY, placeables) }
                }
            }
        }
    )
}

private fun Modifier.lazyLayoutPointerInput(state: LazyLayoutState): Modifier {
    return pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            state.onDrag(IntOffset(0, dragAmount.y.toInt()))
        }
    }
}

private fun Placeable.PlacementScope.placeItem(state: LazyLayoutState, itemY: Int, placeables: List<Placeable>) {
    val yPosition = itemY - state.offsetState.value

    placeables.fastForEach { placeable ->
        placeable.placeRelative(0, yPosition)
    }
}

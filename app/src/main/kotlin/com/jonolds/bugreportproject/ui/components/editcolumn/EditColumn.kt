package com.jonolds.bugreportproject.ui.components.editcolumn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import com.jonolds.bugreportproject.utils.thenIfNotNull
import kotlinx.collections.immutable.ImmutableList


@Composable
fun <T> EditColumn(
	elems: ImmutableList<T>,
	toKey: T.() -> Any,
	modifier: Modifier = Modifier,
	dragState: DragState2? = null,
	contentFactory: @Composable (T) -> Unit
) {

	val targetIdx by remember { derivedStateOf { dragState?.targetIdx } }
	
	SubcomposeLayout(
		modifier = modifier
			.background(Color.Black)
	) { constrs ->
		
		val measurables = elems.map { elem ->
			
			val key = elem.toKey()
			
			subcompose(key) {
				
				Box(
					content = { contentFactory(elem) },
					modifier = Modifier
						.thenIfNotNull(dragState) { dragReorder(it, key) }
				)
			}[0]
		}
		
		val placables = measurables.map { it.measure(constrs) }
		
		dragState?.heights = placables.map { it.height }
		
		var y = 0
		val placements = IntArray(placables.size)
		
		for (i in placements.indices) {
			placements[i] = y
			y+=placables[i].height
		}
		
		
		layout(constrs.maxWidth, placables.sumOf { it.height }) {
			for (i in placables.indices) {
				val pl = placables[i]
				if (i != targetIdx)
					pl.placeRelative(0, placements[i])
			}
			targetIdx?.let {
				placables[it].placeRelative(0, placements[it])
			}
		}
	}
	
}



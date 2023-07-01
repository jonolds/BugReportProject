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
import androidx.compose.ui.zIndex
import com.jonolds.bugreportproject.utils.thenIfNotNull
import kotlinx.collections.immutable.ImmutableList


@Composable
fun <T> EditColumn(
	elems: ImmutableList<T>,
	toKey: T.() -> Any,
	modifier: Modifier = Modifier,
	dragState: DragState2,
	contentFactory: @Composable (T) -> Unit
) {

	
	SubcomposeLayout(
		modifier = modifier
			.background(Color.Black)
	) { constrs ->
		
		
		
		val measurables = elems.mapIndexed { i, elem ->
			
			val key = elem.toKey()
			
			println("measurement")
			subcompose(key) {
				
				Box(
					content = { contentFactory(elem) },
					modifier = Modifier
						.zIndex(if(dragState.targetIdx == i) 1f else 0f)
						.dragReorder(dragState, key)
				)
			}[0]
		}
		
		val placables = measurables.map { it.measure(constrs) }
		
		dragState.heights = placables.map { it.height }
		
		var y = 0
		layout(constrs.maxWidth, placables.sumOf { it.height }) {
			println("placement")
			for (i in placables.indices) {
				placables[i].placeRelative(0, y)
				y+=placables[i].height
			}
		}
	}
	
}



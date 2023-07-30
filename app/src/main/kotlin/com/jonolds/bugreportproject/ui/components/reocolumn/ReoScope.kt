package com.jonolds.bugreportproject.ui.components.reocolumn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.BeyondBoundsLayout
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalProvider
import com.jonolds.bugreportproject.ui.components.DragItemData2
import com.jonolds.bugreportproject.ui.components.LocalDragItemData2
import com.jonolds.bugreportproject.utils.thenIf

@Stable
class ReoScope(
    val state: ReoState,
    val useHandleMod: Boolean,
) {
    private var lazyListScope: LazyListScope? = null

    private var beforeReorderCount = 0

	private var reorderElemsAdded = false

    lateinit var shadowContentFactory: @Composable (Int) -> Unit


    var keyIdxMap = SnapshotStateMap<Any, Int>()

    fun uniqueItem(
        key: Any,
        content: @Composable LazyItemScope.() -> Unit
    ) {
		if (!reorderElemsAdded)
        	beforeReorderCount++

        with(lazyListScope!!) {
            item(
                key = key,
                contentType = key,
                content = content
            )
        }
    }

    fun <T> reorderable(
        elems: List<T>,
        toKey: (T) -> Any,
        move: (from: Int, to: Int) -> Unit,
        contentFactory: @Composable (T) -> Unit
    ) {

		reorderElemsAdded = true

        elems.forEachIndexed { i, elem -> keyIdxMap[toKey(elem)] = i+beforeReorderCount }

        shadowContentFactory = { targetIdx ->
			val elem = elems[targetIdx -beforeReorderCount]
			contentFactory(elem)
		}

        state.move = { from, to -> move(from-beforeReorderCount, to-beforeReorderCount) }

        with(lazyListScope!!) {

			items(
				items = elems,
				key = { toKey(it)},
				contentType = { ReoContentType.LIST_ITEM },
				itemContent = { elem ->
					ReoElemHolder(
						elem = elem,
						toKey = toKey,
						dragState = state,
						useHandleMod = useHandleMod,
						contentFactory = contentFactory
					)

				}
			)
        }
    }


    fun processContent(lazyListScope: LazyListScope, content: ReoScope.() -> Unit) {
		reorderElemsAdded = false
        beforeReorderCount = 0
        this.lazyListScope = lazyListScope
        content()
    }


	@OptIn(ExperimentalComposeUiApi::class)
	@Composable
	fun <T>ReoElemHolder(
		elem: T,
		toKey: (T) -> Any,
		dragState: ReoState,
		useHandleMod: Boolean,
		contentFactory: @Composable (T) -> Unit,
		isShadow: Boolean = false
	) {

		val dragItemData = remember(elem) {
			val key = toKey(elem)
			DragItemData2(key, { keyIdxMap[key]!! }, dragState, useHandleMod)
		}
		CompositionLocalProvider(LocalDragItemData2 provides dragItemData) {

			Box(
				content = { contentFactory(elem) },
				modifier = Modifier
					.thenIf(!isShadow) { reoItem(dragState) { keyIdxMap[dragItemData.key]!! } }
			)
		}
	}
}


enum class ReoContentType {
    LIST_ITEM, ACTION_ITEM
}
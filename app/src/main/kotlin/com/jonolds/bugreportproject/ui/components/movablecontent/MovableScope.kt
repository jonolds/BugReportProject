package com.jonolds.bugreportproject.ui.components.movablecontent

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateMap

@Stable
class MovableScope<T>(
	val contentMap2: Map<Any, @Composable () -> Unit>,
	val setBeforeReorderCount: (Int) -> Unit,
	val elems: List<T>,
	val toKey: (T) -> Any,
    val state: MovableState,
    val useHandleMod: Boolean,
) {

	init {
		println("new scope")
	}
    private var lazyListScope: LazyListScope? = null

    private var internalBeforeReorderCount = 0

	private var reorderElemsAdded = false


    fun uniqueItem(
        key: Any,
        content: @Composable LazyItemScope.() -> Unit
    ) {

		if (!reorderElemsAdded)
        	internalBeforeReorderCount++

        with(lazyListScope!!) {
            item(
                key = key,
                contentType = key,
                content = content
            )
        }
    }

    fun reorderable(
		elems: List<T>,
		toKey: (T) -> Any
	) {

		setBeforeReorderCount(internalBeforeReorderCount)

		reorderElemsAdded = true

        with(lazyListScope!!) {

			println("items")
			items(
				items = elems,
				key = toKey,
				contentType = { MovableContentType.LIST_ITEM },
				itemContent = {
					println("itemContent")
					contentMap2[toKey(it)]?.invoke()
			  	},
			)
        }
    }


    fun processContent(lazyListScope: LazyListScope, content: MovableScope<T>.() -> Unit) {
		println("process")
		reorderElemsAdded = false
        internalBeforeReorderCount = 0
        this.lazyListScope = lazyListScope
        content()
    }

}


enum class MovableContentType {
    LIST_ITEM, ACTION_ITEM
}
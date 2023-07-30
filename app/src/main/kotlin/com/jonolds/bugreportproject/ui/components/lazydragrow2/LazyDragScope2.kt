package com.jonolds.bugreportproject.ui.components.lazydragrow2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
class LazyDragScope2(
    val state: LazyDragState2,
) {


    var lazyListScope: LazyListScope? = null

    var anchorCount = 0

    var shadowContentFactory: (@Composable (Int) -> Unit)? = null

    fun actionItem(
        key: Any?,
        content: @Composable () -> Unit
    ) {
        anchorCount++

        with(lazyListScope!!) {
            item(
                key = key,
                contentType = LazyDragContentType2.ACTION_ITEM,
                content = { content() }
            )
        }
    }

    fun <T> elemsIndexed(
        elems: List<T>,
        toKey: (T) -> Any,
        move: (from: Int, to: Int) -> Unit,
        contentFactory: @Composable (T) -> Unit
    ) {

        shadowContentFactory = { targetIdx -> contentFactory(elems[targetIdx]) }

        state.reorderRange = anchorCount until anchorCount + elems.size
        state.move = move

        with(lazyListScope!!) {
            itemsIndexed(
                items = elems,
                key = { _, elem -> toKey(elem) },
                contentType = { _, _ -> LazyDragContentType2.LIST_ITEM },
                itemContent = { i, elem ->
                    Box(
                        content = { contentFactory(elem) },
                        modifier = Modifier
                            .lazyDragItem2(state, i)
                    )
                }
            )


        }

    }


    fun processContent(lazyListScope: LazyListScope, content: LazyDragScope2.() -> Unit) {
        anchorCount = 0
        this.lazyListScope = lazyListScope
        content()
    }
}


enum class LazyDragContentType2 {
    LIST_ITEM, ACTION_ITEM
}
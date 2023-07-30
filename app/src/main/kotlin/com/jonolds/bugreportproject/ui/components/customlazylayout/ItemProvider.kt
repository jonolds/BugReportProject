package com.jonolds.bugreportproject.ui.components.customlazylayout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberItemProvider(customLazyListScope: CustomLazyListScope.() -> Unit): ItemProvider {
    
    val customLazyListScopeState = remember { mutableStateOf(customLazyListScope) }
        .apply { 
            value = customLazyListScope 
        }

    return remember {
        ItemProvider(
            derivedStateOf {
                val layoutScope = CustomLazyListScopeImpl()
                    .apply(customLazyListScopeState.value)
                layoutScope.items
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
class ItemProvider(
    private val itemsState: State<List<LazyLayoutItemContent>>
) : LazyLayoutItemProvider {

    
    override val itemCount get() = itemsState.value.size

    @Composable
    override fun Item(index: Int, key: Any) {
        val item = itemsState.value.getOrNull(index)
        item?.itemContent?.invoke(item.itemY)
    }



    fun getItemIndexesInRange(boundaries: IntRect): List<Int> {
        val result = mutableListOf<Int>()

        itemsState.value.forEachIndexed { index, itemContent ->
            val itemY = itemContent.itemY
            if (itemY in boundaries.top..boundaries.bottom)
                result.add(index)
        }

        return result
    }

    fun getItem(index: Int): Int? {
        return itemsState.value.getOrNull(index)?.itemY
    }
}

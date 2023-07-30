package com.jonolds.bugreportproject.ui.components.customlazylayout

interface CustomLazyListScope {

    fun items(items: List<Int>, itemContent: ComposableItemFactory)

}

class CustomLazyListScopeImpl : CustomLazyListScope {

    private val _items = mutableListOf<LazyLayoutItemContent>()
    val items: List<LazyLayoutItemContent> = _items

    override fun items(
        items: List<Int>, 
        itemContent: ComposableItemFactory
    ) {
        items.forEach { _items.add(LazyLayoutItemContent(it, itemContent)) }
    }
}

package com.jonolds.bugreportproject.ui.components

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

@Stable
interface DragStateInterface {

	fun onDragStart(targetIdx: Int)
	fun onDrag(dragAmount: Float)
	fun onDragCancel()
	fun onDragEnd()

	fun getOffset(idx: Int): Int = -1

	val targetIdx: Int?

}

@Stable
data class DragItemData(
	val idx: Int,
	val state: DragStateInterface,
	val useHandleMod: Boolean,
)

@Stable
val LocalDragItemData = compositionLocalOf<DragItemData?> { null }


@Stable
data class DragItemData2(
	val key: Any,
	val getIdxFromKey: (Any) -> Int,
	val state: DragStateInterface,
	val useHandleMod: Boolean,
)

@Stable
val LocalDragItemData2 = compositionLocalOf<DragItemData2?> { null }




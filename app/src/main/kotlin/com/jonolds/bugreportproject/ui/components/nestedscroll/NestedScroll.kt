package com.jonolds.bugreportproject.ui.components.nestedscroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import com.jonolds.bugreportproject.ui.components.editcolumn7.derivedStructural


@Stable
class ScrollPair(
	getOriginInScroll: () -> Int,
	val parentScroll: ScrollState = ScrollState(0),
	val childScroll: ScrollableState = LazyListState()
) {


	var info: WindowInfo? by mutableStateOf(null)

	val originInScroll by derivedStructural { info?.listPosInScroll ?: 0 }


	val distFromEQ by derivedStructural { parentScroll.value-originInScroll }


	val childConn = nestedScrollConnection(
		name = "child",
		onPreScroll = { available, source ->

			if (available.y > 0 && childScroll.canScrollBackward)
				Offset(0f, -childScroll.dispatchRawDelta(-available.y))
			else
				Offset(0f, -childScroll.dispatchRawDelta(-available.y))

	  	},
		onPostScroll = { consumed, available, source ->

			Offset(0f, available.y)
		}
	)


	val parentConn = nestedScrollConnection(
		name = "parent",
		onPreScroll = { available, source ->
			Offset(0f, 0f)
		},
		onPostScroll = { consumed, available, source ->

			Offset(0f, 0f)
		}
	)


}



fun nestedScrollConnection(
	name: String? = null,
	onPreScroll: ((available: Offset, source: NestedScrollSource) -> Offset)? = null,
	onPostScroll: ((consumed: Offset, available: Offset, source: NestedScrollSource) -> Offset)? = null
): NestedScrollConnection = object : NestedScrollConnection {


	override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

		val retConsumed = onPreScroll?.invoke(available, source) ?: -super.onPreScroll(-available, source)
//		println("$name PRE SCROLL  available=${available.y}     retConsumed=${retConsumed.y}")
		return retConsumed
	}

	override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
		val retConsumed = onPostScroll?.invoke(consumed, available, source) ?: super.onPostScroll(consumed, available, source)
//		println("$name POST SCROLL  consumed=${consumed.y}  available=${available.y}     retConsumed=${retConsumed.y}")
		return retConsumed
	}
}




@Composable
fun rememberNestedScrollConnection(dispatcher: NestedScrollDispatcher): NestedScrollConnection = remember {
	nestedScrollConnection(
		name = "child",
		onPreScroll = { available, source ->  dispatcher.dispatchPreScroll(available, source) },
		onPostScroll = { consumed, available, source ->  dispatcher.dispatchPostScroll(consumed, available, source) }
	)
}





@Composable
fun rememberNestedScrollDispatcher(): NestedScrollDispatcher = remember {
	NestedScrollDispatcher()
}
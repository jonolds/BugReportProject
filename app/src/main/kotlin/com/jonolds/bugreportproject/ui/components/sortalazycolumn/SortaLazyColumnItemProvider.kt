package com.jonolds.bugreportproject.ui.components.sortalazycolumn

import android.annotation.SuppressLint
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.runningReduce


@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalCoroutinesApi::class)
@Stable
class SortaLazyItemProvider(
	getRootHeight: () -> Int,
	getScrollValue: () -> Int,
	getOriginInScroll: () -> Int,
	getViewPortHeight: () -> Int,
) {

	val addAmount by derivedStateOf { getRootHeight()/3 }

	private val scrollValue by derivedStateOf { getScrollValue() }

	private val originInScroll by derivedStateOf { getOriginInScroll() }

	val viewPortHeight by derivedStateOf { getViewPortHeight() }

	val visibleLen get() = scrollValue - originInScroll + viewPortHeight

	var reportedAvg by mutableStateOf<Int?>(null)

	val maxScrollUserInput = MutableStateFlow(BUFFER_LENGTH_PX)

	private val maxScrollValueToLoad = snapshotFlow { visibleLen }
		.runningReduce { oldValue, newValue -> maxOf(oldValue, newValue) }
		.mapLatest { it.coerceAtLeast(0) }
		.distinctUntilChanged()

	val maxScrollFinal = maxScrollValueToLoad.combine(maxScrollUserInput) { orig, userInput ->
		maxOf(orig, userInput)
	}

	val numIdxsToLoad = maxScrollFinal.mapLatest{ maxScroll -> if (reportedAvg == null || reportedAvg == 0) 1 else maxScroll/reportedAvg!! }
		.distinctUntilChanged()
		.flowOn(Dispatchers.Default)

}


const val BUFFER_LENGTH_PX = 800
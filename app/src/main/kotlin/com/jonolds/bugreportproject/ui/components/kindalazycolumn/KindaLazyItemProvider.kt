package com.jonolds.bugreportproject.ui.components.kindalazycolumn

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
class KindaLazyItemProvider(
	getKeys: () -> List<Any>,
	getScrollValue: () -> Int,
	getOriginInScroll: () -> Int,
	getViewPortHeight: () -> Int,
) {


	private val scrollValue by derivedStateOf { getScrollValue() }

	private val originInScroll by derivedStateOf { getOriginInScroll() }

	private val viewPortHeight by derivedStateOf { getViewPortHeight() }

	private val keys by derivedStateOf { getKeys() }

	private var reportedAvg by mutableStateOf(0)

	val avg by derivedStateOf { if (reportedAvg == 0) DEFAULT_AVG else reportedAvg }


	private val maxScrollValueToLoad = snapshotFlow {
		println(scrollValue - originInScroll + viewPortHeight)
		scrollValue - originInScroll + viewPortHeight }
		.runningReduce { oldValue, newValue -> maxOf(oldValue, newValue) }
		.mapLatest { it.coerceAtLeast(0) }
		.distinctUntilChanged()

	val maxScrollUserInput = MutableStateFlow(BUFFER_LENGTH_PX)

	val maxScrollFinal = maxScrollValueToLoad.combine(maxScrollUserInput) { orig, userInput ->
		maxOf(orig, userInput)
	}


	fun incrementMaxScrollUserInput() {
		maxScrollUserInput.value+= BUFFER_LENGTH_PX
	}


	val numIdxsToLoad = maxScrollFinal.mapLatest{ maxScroll -> maxScroll/avg }
		.combine(snapshotFlow { keys.size }) { calculatedNum, numKeys -> minOf(calculatedNum, numKeys) }
		.distinctUntilChanged()
		.flowOn(Dispatchers.Default)



	fun updateAvg(avg: Int) {
		reportedAvg = avg
	}

}


const val BUFFER_LENGTH_PX = 400
const val DEFAULT_AVG = 200
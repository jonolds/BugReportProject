package com.jonolds.bugreportproject.ui.components.nestedscroll

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.ui.layout.LayoutCoordinates
import com.jonolds.bugreportproject.ui.components.editcolumn7.derivedStructural
import com.jonolds.bugreportproject.ui.components.editcolumn7.findParentScrollCoors
import com.jonolds.bugreportproject.ui.components.editcolumn7.localYPosOf


@Stable
class WindowInfo(
	getListCoors: () -> LayoutCoordinates
) {

	val listCoors by derivedStateOf { getListCoors() }
	val scrollCoors by derivedStateOf { listCoors.findParentScrollCoors() ?: listCoors.parentLayoutCoordinates }
	val windowCoors by derivedStateOf {
		scrollCoors?.parentLayoutCoordinates
			?: listCoors.parentLayoutCoordinates
			?: listCoors
	}


	val listPosInScroll by derivedStructural { scrollCoors?.localYPosOf(listCoors, 0) ?: 0}


	val viewPortBounds by derivedStructural {
		windowCoors.localBoundingBoxOf(scrollCoors ?: listCoors)
	}


	val viewPortHeight by derivedStructural { viewPortBounds.height }



}


@Stable
fun <T> derivedReferential(
	calculation: () -> T
): State<T> = derivedStateOf(referentialEqualityPolicy()) { calculation() }


@Stable
fun <T> derivedNeverEqual(
	calculation: () -> T
): State<T> = derivedStateOf(neverEqualPolicy()) { calculation() }
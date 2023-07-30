package com.jonolds.bugreportproject.tests.movablecontenttest

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.tests.trackstest.TracksTestVM
import com.jonolds.bugreportproject.ui.components.TrackCard2
import com.jonolds.bugreportproject.ui.components.editcolumn7.EmptyLayoutCoordinates
import com.jonolds.bugreportproject.ui.components.nestedscroll.ScrollPair
import com.jonolds.bugreportproject.ui.components.nestedscroll.WindowInfo
import com.jonolds.bugreportproject.ui.components.nestedscroll.derivedNeverEqual
import com.jonolds.bugreportproject.ui.components.sortalazycolumn.getMovable
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.SpacerBox
import com.jonolds.bugreportproject.utils.clearFocusOnKeyboardDismiss
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


@Composable
fun MovableTest(vm: TracksTestVM) {

	Column(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.background(Color.Black)
			.fillMaxSize()
	) {


		SpacerBox(ClubhouseColors.darkRed, 150.dp)


		Column(
			modifier = Modifier
				.weight(1f, false)
				.verticalScroll(rememberScrollState())
		) {


			SpacerBox(ClubhouseColors.purple, 120.dp)


//			RegularList(vm = vm)
			MovableContentList(vm = vm)
//			LazyColumnList(vm = vm)

			SpacerBox(ClubhouseColors.purple, 120.dp)
		}

		SpacerBox(ClubhouseColors.darkGreen, 150.dp)

	}
}


@Composable
fun MovableContentList(vm: TracksTestVM) {


	val movables = vm.tracks.getMovable(toKey = { it.uuid }) { elem ->
		TrackCard2(elem)
	}
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {

		for (i in vm.tracks.indices) {
			movables(vm.tracks[i])
		}

	}


}

@Composable
fun RegularList(vm: TracksTestVM) {


	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {

		val time = measureTimeMillis {
			for (i in vm.tracks.indices) {
//				println("new card $i")

				TrackCard2(vm.tracks[i])
			}
		}

		println(time)

	}
}


@Composable
fun LazyColumnList(vm: TracksTestVM) {

	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(0.dp, 10000.dp)
	) {

		val time = System.currentTimeMillis()

		items(
			count = vm.tracks.size,
			key = { vm.tracks[it].uuid },
			contentType = { vm.tracks[it].artistCreditEn },
			itemContent = {
				println("new card $it   elapsed=${System.currentTimeMillis()-time}")
				TrackCard2(vm.tracks[it])
			}
		)
	}

}








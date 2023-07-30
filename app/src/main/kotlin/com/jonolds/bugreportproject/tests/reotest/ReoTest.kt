package com.jonolds.bugreportproject.tests.reotest


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.model.TracksItemObservable
import com.jonolds.bugreportproject.tests.trackstest.TracksTestVM
import com.jonolds.bugreportproject.ui.components.TrackCard
import com.jonolds.bugreportproject.ui.components.TrackCard2
import com.jonolds.bugreportproject.ui.components.reocolumn.ReoColumn
import com.jonolds.bugreportproject.ui.components.reocolumn.reoHandle
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.SpacerBox
import com.jonolds.bugreportproject.utils.clearFocusOnClick


@Composable
fun ReoTest(vm: TracksTestVM) {


	Column(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.background(Color.Black)
			.fillMaxSize()
//			.clearFocusOnClick()
	) {

		SpacerBox(ClubhouseColors.darkRed, 240.dp)

		ReoColumn(
			useHandleMod = true,
			content = {
				uniqueItem("greenBox") {
					SpacerBox(ClubhouseColors.darkGreen, 220.dp)
				}

				uniqueItem("blueBox") {
					SpacerBox(ClubhouseColors.seedBlue, 100.dp)
				}

				reorderable(
					elems = vm.tracks,
					toKey = { it.position },
					move = vm::move,
					contentFactory = { track ->

						TrackCard(track = track)
					}
				)


				uniqueItem("purpleBox") {
					SpacerBox(ClubhouseColors.purple, 120.dp)
				}


			},
			modifier = Modifier
		)

		SpacerBox(ClubhouseColors.darkGreen, 150.dp)

	}
}

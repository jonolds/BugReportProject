package com.jonolds.bugreportproject.tests.customlazytest

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.tests.trackstest.TracksTestVM
import com.jonolds.bugreportproject.ui.components.SwipeAway
import com.jonolds.bugreportproject.ui.components.TrackCard
import com.jonolds.bugreportproject.ui.components.customlazylayout.CustomLazyLayout
import com.jonolds.bugreportproject.ui.components.editcolumn7.DragColumn7
import com.jonolds.bugreportproject.ui.components.rememberAutoScrollConfig
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.SpacerBox
import com.jonolds.bugreportproject.utils.clearFocusOnClick


@Composable
fun CustomLazyTest(
	vm: TracksTestVM
) {

	Column(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.background(Color.Black)
			.fillMaxSize()
			.clearFocusOnClick()
	) {


		SpacerBox(color = ClubhouseColors.darkGreen, height = 250.dp)

		val scrollState = rememberScrollState()
		Column(
			modifier = Modifier
				.padding(horizontal = 8.dp, vertical = 8.dp)
				.fillMaxWidth()
				.weight(1f, false)
				.verticalScroll(scrollState)
		) {

			CustomLazyLayout(
				content = {
					items(
						items = vm.tracks.indices.toList(),
						itemContent = {
							TrackCard(track = vm.tracks[it])
						}
					)
				},
				modifier = Modifier
					.padding(8.dp)
					.fillMaxWidth()
			)
			DragColumn7(
				elems = vm.tracks,
				toKey = { it.position },
				useHandleMod = true,
				move = vm::move,
				scrollState = scrollState,
				scrollConfig = rememberAutoScrollConfig(),
				contentFactory = { track ->
//					println("new card")
					TrackCard(track = track)
				},
				modifier = Modifier
					.padding(8.dp)
					.fillMaxWidth()
			)

		}

		SpacerBox(color = ClubhouseColors.darkGreen, height = 150.dp)
	}
}
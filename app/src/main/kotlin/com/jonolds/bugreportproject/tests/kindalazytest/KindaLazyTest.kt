package com.jonolds.bugreportproject.tests.kindalazytest

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.jonolds.bugreportproject.ui.components.kindalazycolumn.KindaLazyColumn
import com.jonolds.bugreportproject.ui.components.movablecontent.MovableColumn
import com.jonolds.bugreportproject.ui.components.rememberAutoScrollConfig
import com.jonolds.bugreportproject.ui.components.reocolumn.ReoColumn
import com.jonolds.bugreportproject.ui.components.reocolumn.reoHandle
import com.jonolds.bugreportproject.ui.components.sortalazycolumn.SortaLazyColumn
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.SpacerBox


@Composable
fun KindaLazyTest(vm: TracksTestVM) {


	Column(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.background(Color.Black)
			.fillMaxSize()
//			.clearFocusOnClick()
	) {

		SpacerBox(ClubhouseColors.darkRed, 140.dp)


		val scrollState = rememberScrollState()
		Column(
			modifier = Modifier
				.padding(horizontal = 8.dp, vertical = 8.dp)
				.fillMaxWidth()
				.weight(1f, false)
				.verticalScroll(scrollState)
		) {


			SpacerBox(ClubhouseColors.purple, 100.dp)

			SortaLazyColumn(
				elems = vm.tracks,
				toKey = { it.position },
				move = vm::move,
				scrollState = scrollState,
				scrollConfig = rememberAutoScrollConfig(),
				contentFactory =  { track ->
			  		TrackCard(track = track)
				},
				modifier = Modifier
					.padding(8.dp)
					.fillMaxWidth()
			)


			SpacerBox(ClubhouseColors.purple, 100.dp)

		}


		SpacerBox(ClubhouseColors.darkGreen, 150.dp)

	}
}
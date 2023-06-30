package com.jonolds.bugreportproject.ui

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.ui.components.TrackCard
import com.jonolds.bugreportproject.ui.components.editcolumn.EditColumn
import com.jonolds.bugreportproject.ui.components.editcolumn.DragState2
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.clearFocusOnClick

@Composable
fun TracksTest(vm: TracksTestVM) {
	
	val scrollState = rememberScrollState()
	
	Column(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.fillMaxSize()
			.background(Color.Black)
			.verticalScroll(scrollState)
			.clearFocusOnClick()
	) {
		
		Column(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
		) {
			
			Box(
				modifier = Modifier
					.background(ClubhouseColors.darkGreen)
					.fillMaxWidth()
					.requiredHeight(390.dp)
			)
			
			
			val dragState = remember {
				DragState2(
					move = { from, to -> vm.move(from, to) },
					scroll = scrollState,
					getKeys = { vm.tracks.map { it.uuid }}
				)
			}
			
			
			EditColumn(
				elems = vm.tracks,
				toKey = { uuid },
				modifier = Modifier
					.fillMaxWidth(),
				dragState = dragState
			) { track -> TrackCard(track = track) }
		}
	}
}







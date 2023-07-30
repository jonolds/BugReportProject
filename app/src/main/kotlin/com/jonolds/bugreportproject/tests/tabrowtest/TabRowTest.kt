package com.jonolds.bugreportproject.tests.tabrowtest


import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayoutState
import androidx.compose.ui.layout.SubcomposeSlotReusePolicy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.model.DiscsItemObservable
import com.jonolds.bugreportproject.model.TextJNullable
import com.jonolds.bugreportproject.ui.components.NoRippleNoMinTouchSize
import com.jonolds.bugreportproject.ui.components.SwipeAway
import com.jonolds.bugreportproject.ui.components.TrackCard
import com.jonolds.bugreportproject.ui.components.TrackCard2
import com.jonolds.bugreportproject.ui.components.editcolumn7.DragColumn7
import com.jonolds.bugreportproject.ui.components.kindalazycolumn.KindaLazyColumn
import com.jonolds.bugreportproject.ui.components.rememberAutoScrollConfig
import com.jonolds.bugreportproject.ui.components.sortalazycolumn.SortaLazyColumn
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.ui.theme.ClubhouseDarkColorScheme
import com.jonolds.bugreportproject.utils.SpacerBox
import com.jonolds.bugreportproject.utils.clearFocusOnClick
import com.jonolds.bugreportproject.utils.move


@Composable
fun TabRowTest(vm: TabRowTestVM) {


	Column(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.background(Color.Black)
			.fillMaxSize()
			.clearFocusOnClick()
	) {


		val scrollState = rememberScrollState()
		Column(
			modifier = Modifier
				.padding(horizontal = 8.dp, vertical = 8.dp)
				.fillMaxWidth()
				.weight(1f, false)
				.verticalScroll(scrollState)
		) {


			SpacerBox(color = ClubhouseColors.darkGreen, height = 250.dp)

			Discs(
				discs = vm.discs,
				getScrollState = { scrollState }
			)

		}

		SpacerBox(color = ClubhouseColors.darkGreen, height = 150.dp)

	}
}


@Composable
fun Discs(
	discs: List<DiscsItemObservable>,
	getScrollState: () -> ScrollState,
) {

	NoRippleNoMinTouchSize {

		Column(modifier = Modifier.fillMaxHeight()) {


			val selectedTab = remember { mutableIntStateOf(0) }


			TabRow2(
				numTabs = discs.size,
				getSelected = { selectedTab.intValue },
				setSelected = { selectedTab.intValue = it },
				modifier = Modifier
					.padding(vertical = 8.dp, horizontal = 4.dp)
			)



			DiscLabel(discs = discs, getSelected = {selectedTab.intValue})

			DragColumn7(
				elems = discs[selectedTab.intValue].tracks,
				toKey = { it.position },
				move = { from, to -> discs[selectedTab.intValue].tracks.move(from, to)},
				useHandleMod = true,
				scrollState = remember { getScrollState() },
				scrollConfig = rememberAutoScrollConfig(),

				modifier = Modifier
					.padding(8.dp)
			) { track ->

				SwipeAway(onDismiss = { discs[selectedTab.intValue].tracks.remove(track) }) {
					TrackCard2(track = track)
				}

			}

		}
	}
}


@Composable
fun DiscLabel(
	getSelected: () -> Int,
	discs: List<DiscsItemObservable>
) {

	if (discs.size <= 1)
		return

	val selected by remember { derivedStateOf { getSelected() }}


	TextJNullable(
		label = "Disc Title",
		initialValue = discs[selected].discTitle,
		onValueChange = { discs[selected].discTitle = it },
		modifier = Modifier
			.padding(8.dp),
	)
}
package com.jonolds.bugreportproject.tests.lazydragrowtest

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.ui.components.AutoScrollConfig
import com.jonolds.bugreportproject.ui.components.COLOR_CARD_HEIGHT
import com.jonolds.bugreportproject.ui.components.COLOR_CARD_PADDING
import com.jonolds.bugreportproject.ui.components.ColorCard
import com.jonolds.bugreportproject.ui.components.ColorCardFooterContent
import com.jonolds.bugreportproject.ui.components.SwipeAway
import com.jonolds.bugreportproject.ui.components.lazydragrow2.LazyDragRow2
import com.jonolds.bugreportproject.ui.theme.ClubhouseDarkColorScheme


@Composable
fun LazyDragRowTest(
	vm: LazyDragRowVM
) {

	Column(
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.background(ClubhouseDarkColorScheme.surface)
			.fillMaxSize()
	) {


		LazyDragRow2(
			autoScrollConfig = AutoScrollConfig(10, 70, 1000),
			content =  {

				elemsIndexed(
					elems = vm.colors,
					toKey = { it.first.toArgb() },
					move = vm::move,
					contentFactory = { elem ->
						SwipeAway(
							onDismiss = { vm.removeElem(elem.first.toArgb()) },
							pctThreshold = .8f,
							orientation = Orientation.Vertical,
							reverseDirection = true,
						) {
							ColorCard(elem = elem.first, size = elem.second)
						}

					}
				)

				actionItem(
					key = "footer",
					content = {
						ColorCardFooterContent(
							getRandomColor = vm::randomColor,
							modifier = Modifier
								.padding(COLOR_CARD_PADDING)
								.size(COLOR_CARD_HEIGHT)
						)
					}
				)

			},
			modifier = Modifier
				.background(Color.Black)
				.padding(12.dp)
		)


	}

}


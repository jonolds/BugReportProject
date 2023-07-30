package com.jonolds.bugreportproject.tests.tabrowtest

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jonolds.bugreportproject.ui.theme.ClubhouseDarkColorScheme
import kotlin.math.roundToInt


@Composable
fun TabRow2(
	numTabs: Int,
	getSelected: () -> Int,
	setSelected: (Int) -> Unit,
	modifier: Modifier = Modifier,
	containerColor: Color = TabRowDefaults.containerColor,
	contentColor: Color = TabRowDefaults.contentColor,
	dividerColor: Color = ClubhouseDarkColorScheme.outlineVariant,
	dividerHeight: Dp = 1.dp,
	indicatorColor: Color = ClubhouseDarkColorScheme.primary,
	indicatorHeight: Dp = 2.dp,
	labelContent: @Composable (Int) -> Unit = { i ->
		Text(
			text = i.toString(),
			color = contentColor,
			modifier = Modifier
				.padding(vertical = 12.dp)
		)
	}
) {



	BoxWithConstraints(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.background(color = containerColor),
		content =  {


			TabRowDivider(dividerColor = dividerColor, dividerHeight = dividerHeight)


			TabRowIndicator(
				getSelected = getSelected,
				indicatorColor = indicatorColor,
				indicatorHeight = indicatorHeight,
				width = maxWidth/numTabs
			)


			Row {
				for (i in 0 until numTabs) {
					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.fillMaxWidth(1.0f / (numTabs - i))
							.clickable { setSelected(i) }
					) {
						labelContent(i)
					}
				}
			}



		}
	)

}


@Composable
internal fun BoxWithConstraintsScope.TabRowIndicator(
	getSelected: () -> Int,
	indicatorColor: Color,
	indicatorHeight: Dp,
	width: Dp
) {

	val density = LocalDensity.current
	val widthPx = remember(width) { with(density) { width.toPx()} }

	val selected by remember { derivedStateOf { getSelected() }}


	val offset by remember { derivedStateOf {
		(selected*widthPx).roundToInt()
	} }

	val indicatorOffset by animateIntAsState(
		targetValue = offset,
		animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
	)

	Box(
		modifier = Modifier
			.align(Alignment.BottomStart)
			.offset {
				IntOffset(indicatorOffset, 0)
			}
			.width(width)
			.background(color = indicatorColor)
			.height(indicatorHeight)
	)
}


@Composable
internal fun BoxWithConstraintsScope.TabRowDivider(
	dividerColor: Color,
	dividerHeight: Dp
) {
	Box(
		modifier = Modifier
			.align(Alignment.BottomStart)
			.fillMaxWidth()
			.background(color = dividerColor)
			.height(dividerHeight)
	)
}
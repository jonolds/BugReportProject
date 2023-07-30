package com.jonolds.bugreportproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val COLOR_CARD_HEIGHT = 75.dp

val COLOR_CARD_PADDING = 4.dp


@Composable
fun ColorCard(
	elem: Color,
	modifier: Modifier = Modifier,
	i: Int? = null,
	getIsTarget: (() -> Boolean)? = null,
	size: Dp? = null,
) {

	Box(
		contentAlignment = Alignment.Center,
		content =  {
			i?.let {
				Text(
					text = it.toString(),
					fontSize = 7.sp,
					color = Color.White,
					modifier = Modifier
						.background(Color.DarkGray)
				)
			}
			val isTarget by remember(i, elem.toArgb()) { derivedStateOf { getIsTarget?.invoke() ?: false } }
			if (isTarget)
				Bullseye(modifier = Modifier.align(Alignment.TopStart))

		},
		modifier = modifier
			.padding(COLOR_CARD_PADDING)
			.requiredSize(width = size ?: 75.dp, height = COLOR_CARD_HEIGHT)
			.background(elem)
	)

}



@Composable
fun ColorCardFooterContent(
	getRandomColor: () -> Color,
	modifier: Modifier = Modifier
) {

	var backGroundColor by remember { mutableStateOf(getRandomColor()) }

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.background(backGroundColor)
			.clickable {
				backGroundColor = getRandomColor()
			}
	) {
		Text(
			"FOOTER",
			color = Color.White,
			fontSize = 12.sp,
			modifier = Modifier
				.background(Color.DarkGray)
		)
	}

}
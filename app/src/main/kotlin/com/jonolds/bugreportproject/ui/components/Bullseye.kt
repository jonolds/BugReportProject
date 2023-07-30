package com.jonolds.bugreportproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun Bullseye(
	modifier: Modifier = Modifier
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.padding(4.dp)
	) {
		Circle(color = Color.Black, size = 20.dp)
		Circle(color = Color.White, size = 11.dp)
		Circle(color = Color.Red, size = 6.5.dp)
	}

}


@Composable
fun Circle(
	color: Color,
	size: Dp,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.background(color, RoundedCornerShape(20.dp))
			.size(size)
	)
}
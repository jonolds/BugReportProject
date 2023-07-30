package com.jonolds.bugreportproject.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonolds.bugreportproject.R
import com.jonolds.bugreportproject.model.TracksItemObservable
import com.jonolds.bugreportproject.ui.components.editcolumn7.dragItemHandle7
import com.jonolds.bugreportproject.ui.components.movablecontent.movableHandle
import com.jonolds.bugreportproject.ui.components.reocolumn.reoHandle
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.clearFocusOnClick


@Composable
fun TrackCard(
	track: TracksItemObservable,
	modifier: Modifier = Modifier
) {
//	println("new card ${track.position}")


	HorizontalCardWithPadding(
		contentPading = PaddingValues(vertical = if (track.artistCreditEn) 4.dp else 8.dp, horizontal = 4.dp),
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp)
			.background(ClubhouseColors.seedDarkBlue, shape = RoundedCornerShape(12.dp))
	) {


		TestTrackField(
			initialValue = track.position,
			onValueChange = { track.position = it.orEmpty() },
			unfocusedIndicatorLineThickness = 0.dp,
			modifier = Modifier
				.padding(start = 8.dp, end = 12.dp)
				.width(24.dp)
		)

		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
		) {
			TrackAndArtistRow(
				track = track,
				modifier = Modifier
					.weight(1f)
			)


			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween,
				content = {
					ArtistCreditEnButton(
						track = track,
						modifier = Modifier
							.padding(start = 4.dp, end = 8.dp)
							.size(24.dp)
					)
					DragHandle(
						modifier = Modifier
							.dragItemHandle7()
					)
				},
				modifier = Modifier
					.padding(start = 8.dp)
			)
		}
	}

}


@Composable
fun DragHandle(
	modifier: Modifier = Modifier
) {

	Column(
		modifier = modifier
	) {
		Icon(
			painter = painterResource(id = R.drawable.baseline_drag_handle_24_black),
			contentDescription = "Drag Track",
			tint = Color.White.copy(alpha = .5f),
			modifier = Modifier
				.padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
		)
	}
}


@Composable
fun TrackAndArtistRow(
	track: TracksItemObservable,
	modifier: Modifier = Modifier
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
	) {

		var titleTextValue by remember(track.uuid) { mutableStateOf(TextFieldValue(track.title)) }

		Column(
			modifier = Modifier
				.weight(1f, false)
		) {
			TestTrackField(
				initialValue = titleTextValue,
				onValueChange = {
					titleTextValue = it
					track.title = it.text
				},
				modifier = Modifier
					.padding(bottom = if (track.artistCreditEn) 0.dp else 4.dp)
					.onFocusChanged {
						if (!it.hasFocus)
							titleTextValue = titleTextValue.copy(selection = TextRange.Zero)
					}
			)

			if (track.artistCreditEn)
				TestTrackField(
					initialValue = track.artistCredit,
					onValueChange = { track.artistCredit = it },
					fontSize = 12.sp,
					fontStyle = FontStyle.Italic,
					unfocusedIndicatorLineThickness = .3.dp
				)
		}

		Splitter(
			titleTextValue = titleTextValue,
			doSplit = { suffix ->

				track.artistCredit = "$suffix ${track.artistCredit}".trim()

				track.title = titleTextValue.text.removeSuffix(suffix).trim()

				titleTextValue = TextFieldValue(track.title, TextRange(track.title.length))

				track.artistCreditEn = true

			}
		)
	}

}



@Composable
fun ArtistCreditEnButton(
	track: TracksItemObservable,
	modifier: Modifier = Modifier
) {
	val focus = LocalFocusManager.current
	OutlinedIconToggleButton(
		checked = track.artistCreditEn,
		onCheckedChange = { focus.clearFocusOnClick { track.artistCreditEn = !track.artistCreditEn } },
		shape = CutCornerShape(0.dp),
		border = BorderStroke(.5.dp, SolidColor(Color.Black)),
		colors = IconButtonDefaults.outlinedIconToggleButtonColors(
			containerColor = Color.Transparent,
			checkedContainerColor = Color.Transparent,
			contentColor = Color.White.copy(alpha = .7f),
			checkedContentColor = Color.White.copy(alpha = .7f),
		),
		content = {
			Icon(
				painter = painterResource(id = if (track.artistCreditEn) R.drawable.ic_minus else R.drawable.ic_plus),
				contentDescription = "${if (track.artistCreditEn) "Disable" else "Enable"} Artist Credit",
				modifier = Modifier
			)
		},
		modifier = modifier
	)
}



@Composable
inline fun HorizontalCardWithPadding(
	modifier: Modifier = Modifier,
	contentPading: PaddingValues = PaddingValues(),
	content: @Composable RowScope.() -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			content = content,
			modifier = Modifier
				.padding(contentPading)
		)
	}

}


@Composable
fun Splitter(
	titleTextValue: TextFieldValue,
	doSplit: (suffix: String) -> Unit
) {

	val text = titleTextValue.text
	val selection = titleTextValue.selection


	val splitRequirementsMet = selection.start > 0 &&
		selection.start < text.length &&
		selection.collapsed

	if (!splitRequirementsMet)
		return

	val splitText = text.substring(selection.start)


	OutlinedIconButton(
		onClick = { doSplit(splitText) },
		border = null,
		content = {
			Icon(
				painter = painterResource(id = R.drawable.baseline_airline_stops_24),
				contentDescription = "Split at title cursor and move to artist",
				tint = Color.Green.copy(alpha = .5f),
				modifier = Modifier
					.graphicsLayer {
						rotationZ = -90f
						rotationX = 180f
					}
			)
		},
		modifier = Modifier
			.padding(horizontal = 4.dp)
			.size(28.dp)
	)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTrackField(
	initialValue: TextFieldValue,
	onValueChange: (TextFieldValue) -> Unit,
	modifier: Modifier = Modifier,
	fontSize: TextUnit = 14.sp,
	fontStyle: FontStyle = FontStyle.Normal,
	unfocusedIndicatorLineThickness: Dp = .5.dp,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {


	val trackTextStyle = remember(fontSize, fontStyle) {
		TextStyle(
			fontSize = fontSize,
			fontStyle = fontStyle,
			color = Color.White,
			lineHeight = 46.sp,
			textIndent = TextIndent(1.5.sp)
		)
	}

	BasicTextField(
		value = initialValue,
		onValueChange = onValueChange,
		textStyle = trackTextStyle,
		interactionSource = interactionSource,
		singleLine = true,
		visualTransformation = neverBlankVisualTransformation,
		cursorBrush = remember { SolidColor(Color.White) },
		modifier = modifier
			.width(IntrinsicSize.Max)
			.indicatorLine(
				enabled = true,
				interactionSource = interactionSource,
				colors = TextFieldDefaults.colors(),
				isError = false,
				focusedIndicatorLineThickness = 1.dp,
				unfocusedIndicatorLineThickness = unfocusedIndicatorLineThickness
			)
	) { innerTextField ->

		TextFieldDefaults.DecorationBox(
			value = initialValue.text,
			innerTextField = innerTextField,
			enabled = true,
			singleLine = true,
			visualTransformation = VisualTransformation.None,
			interactionSource = interactionSource,
			contentPadding = remember { PaddingValues(start = 1.dp, end = 1.dp) },
			container = {}
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTrackField(
	initialValue: String?,
	onValueChange: (String?) -> Unit,
	modifier: Modifier = Modifier,
	fontSize: TextUnit = 14.sp,
	fontStyle: FontStyle = FontStyle.Normal,
	unfocusedIndicatorLineThickness: Dp = .5.dp,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {

	val localTextStyle = LocalTextStyle.current

	val trackTextStyle = remember(fontSize, fontStyle) {
		localTextStyle.copy(
			fontSize = fontSize,
			fontStyle = fontStyle,
			color = Color.White,
			textIndent = TextIndent(1.5.sp)
		)
	}

	BasicTextField(
		value = initialValue.orEmpty(),
		onValueChange = onValueChange,
		textStyle = trackTextStyle,
		interactionSource = interactionSource,
		singleLine = true,
		cursorBrush = remember { SolidColor(Color.White) },
		modifier = modifier
//			.width(IntrinsicSize.Max)
			.indicatorLine(
				enabled = true,
				interactionSource = interactionSource,
				colors = TextFieldDefaults.colors(),
				isError = false,
				focusedIndicatorLineThickness = 1.dp,
				unfocusedIndicatorLineThickness = unfocusedIndicatorLineThickness
			)
			.defaultMinSize(minWidth = 20.dp, minHeight = 5.dp)
	) { innerTextField ->

		TextFieldDefaults.DecorationBox(
			value = initialValue.orEmpty(),
			innerTextField = innerTextField,
			enabled = true,
			singleLine = true,
			visualTransformation = VisualTransformation.None,
			interactionSource = interactionSource,
			contentPadding = remember { PaddingValues(start = 1.dp, end = 1.dp) },
			container = {}
		)
	}
}



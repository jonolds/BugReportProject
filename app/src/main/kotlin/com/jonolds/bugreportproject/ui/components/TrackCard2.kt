package com.jonolds.bugreportproject.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
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
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonolds.bugreportproject.R
import com.jonolds.bugreportproject.model.TracksItemObservable
import com.jonolds.bugreportproject.ui.components.editcolumn7.dragItemHandle7
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.clearFocusOnClick


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackCard2(
	track: TracksItemObservable,
	modifier: Modifier = Modifier
) {

	val focusManager = LocalFocusManager.current

	var isExpanded by remember(track) { mutableStateOf<Int?>(null) }

	fun doExpand(requesterIdx: Int) {
		isExpanded = requesterIdx
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp, horizontal = 12.dp)
			.background(ClubhouseColors.seedDarkBlue, shape = RoundedCornerShape(12.dp))
			.clickable { focusManager.clearFocus() }
			.onFocusChanged {
				if (!it.hasFocus)
					isExpanded = null
			}
			.focusGroup()
	) {

		ClearFocusWhenImeClosed(isExpanded != null)

		TestTrackField2(
			isExpanded = isExpanded,
			doExpand = { doExpand(0) },
			fieldIdx = 0,
			initialValue = track.position,
			onValueChange = { track.position = it.orEmpty() },
			modifier = Modifier
				.padding(start = 8.dp, end = 12.dp)
				.width(24.dp)
		)


		val titleArtistPadding by rememberUpdatedState(newValue = when (track.artistCreditEn) {
				true -> PaddingValues(vertical = 4.dp)
				else -> PaddingValues(vertical = 8.dp)
		})

		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(1f, false)
				.fillMaxWidth()
				.padding(titleArtistPadding)
		) {

			TestTrackField2(
				isExpanded = isExpanded,
				doExpand = { doExpand(1) },
				fieldIdx = 1,
				initialValue = track.title,
				onValueChange = { track.title = it.orEmpty() },
				modifier = Modifier
			)

			if (track.artistCreditEn)
				TestTrackField2(
					isExpanded = isExpanded,
					doExpand = { doExpand(2) },
					fieldIdx = 2,
					initialValue = track.artistCredit,
					onValueChange = { track.artistCredit = it },
					fontSize = 12.sp,
					fontStyle = FontStyle.Italic,
				)


		}



		ArtistCreditEn(track = track)

		DragHandle2(
			modifier = Modifier
				.dragItemHandle7()
		)
	}

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClearFocusWhenImeClosed(
	isExpanded: Boolean
) {

	if (isExpanded) {
		var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
		val imeIsVisible = WindowInsets.isImeVisible
		val focusManager = LocalFocusManager.current
		LaunchedEffect(imeIsVisible) {
			if (imeIsVisible) {
				keyboardAppearedSinceLastFocused = true
			} else if (keyboardAppearedSinceLastFocused) {
				focusManager.clearFocus()
			}
		}
	}

}


@Composable
fun TestTrackField2(
	isExpanded: Int?,
	doExpand: () -> Unit,
	fieldIdx: Int,
	initialValue: String?,
	onValueChange: (String?) -> Unit,
	modifier: Modifier = Modifier,
	fontSize: TextUnit = 14.sp,
	fontStyle: FontStyle = FontStyle.Normal
) {

	val requester = remember(fieldIdx) { FocusRequester() }

	val localTextStyle = LocalTextStyle.current
	val trackTextStyle = remember(fontSize, fontStyle) {
		localTextStyle.copy(
			fontSize = fontSize,
			fontStyle = fontStyle,
			color = Color.White,
			textIndent = TextIndent(1.5.sp)
		)
	}

	if (isExpanded != null) {
		TestTrackField(
			initialValue = initialValue,
			onValueChange = onValueChange,
			textStyle = trackTextStyle,
			modifier = modifier
				.focusRequester(requester)
		)
		if (fieldIdx == isExpanded)
			LaunchedEffect(Unit) {
				requester.requestFocus()
			}
	}
	else
		Text(
			style = trackTextStyle,
			text = initialValue.orEmpty(),
			fontSize = fontSize,
			maxLines = 1,
			color = Color.White,
			fontStyle = fontStyle,
			modifier = modifier
				.padding(start = 1.dp, end = 1.dp)
				.clickable(onClick = doExpand)
		)

}


@Composable
fun ArtistCreditEn(
	track: TracksItemObservable
) {

	val focus = LocalFocusManager.current
	OutlinedIconToggleButton(
		checked = track.artistCreditEn,
		onCheckedChange = { focus.clearFocusOnClick { track.artistCreditEn = !track.artistCreditEn } },
		shape = CutCornerShape(0.dp),
		border = BorderStroke(.5.dp, SolidColor(Color.Black)),
		colors = artistCreditButtonColors(),
		content = {
			Icon(
				painter = painterResource(id = if (track.artistCreditEn) R.drawable.ic_minus else R.drawable.ic_plus),
				contentDescription = "${if (track.artistCreditEn) "Disable" else "Enable"} Artist Credit",
			)
		},
		modifier = Modifier
			.padding(start = 12.dp, end = 8.dp)
			.size(30.dp)
	)

}


@Composable
fun artistCreditButtonColors() = IconButtonDefaults.outlinedIconToggleButtonColors(
	containerColor = Color.Transparent,
	checkedContainerColor = Color.Transparent,
	contentColor = Color.White.copy(alpha = .7f),
	checkedContentColor = Color.White.copy(alpha = .7f),
)


@Composable
fun DragHandle2(
	modifier: Modifier = Modifier
) {

	Column(
		verticalArrangement = Arrangement.Center,
		modifier = modifier
			.fillMaxHeight()
	) {
		Icon(
			painter = painterResource(id = R.drawable.baseline_drag_handle_24_black),
			contentDescription = "Drag Track",
			tint = Color.White.copy(alpha = .5f),
			modifier = Modifier
				.padding(top = 4.dp, bottom = 4.dp, end = 8.dp)
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTrackField(
	initialValue: String?,
	onValueChange: (String?) -> Unit,
	textStyle: TextStyle,
	modifier: Modifier = Modifier,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {

	BasicTextField(
		value = initialValue.orEmpty(),
		onValueChange = onValueChange,
		textStyle = textStyle,
		interactionSource = interactionSource,
		singleLine = true,
		cursorBrush = remember { SolidColor(Color.White) },
		modifier = modifier
			.indicatorLine(
				enabled = true,
				interactionSource = interactionSource,
				colors = TextFieldDefaults.colors(),
				isError = false,
				focusedIndicatorLineThickness = 1.dp,
				unfocusedIndicatorLineThickness = .5.dp,
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



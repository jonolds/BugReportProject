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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonolds.bugreportproject.R
import com.jonolds.bugreportproject.model.TracksItemObservable
import com.jonolds.bugreportproject.ui.theme.ClubhouseColors
import com.jonolds.bugreportproject.utils.clearFocusOnClick


@Composable
fun TrackCard(
	track: TracksItemObservable,
	modifier: Modifier = Modifier
) {
	
	
	HorizontalCardWithPadding(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp)
			.background(ClubhouseColors.seedDarkBlue, shape = RoundedCornerShape(12.dp))
	) {
		
		TestTrackField(
			initialValue = track.position,
			onValueChange = { track.position = it },
			showIndicator = false,
			modifier = Modifier
				.padding(start = 8.dp, end = 12.dp, bottom = if (track.artistCreditEn) 0.dp else 4.dp)
				.width(20.dp)
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
				verticalAlignment = Alignment.Top,
				horizontalArrangement = Arrangement.SpaceBetween,
				content = {
					ArtistCreditEnButton(track = track)
					DragHandle()
				}
			)
		}
	}
	
}


@Composable
fun DragHandle() {
	Icon(
		painter = painterResource(id = R.drawable.baseline_drag_handle_24_black),
		contentDescription = "Drag Track",
		tint = Color.White.copy(alpha = .5f),
		modifier = Modifier
			.padding(horizontal = 4.dp)
			.size(28.dp)
	)
}


@Composable
inline fun HorizontalCardWithPadding(
	modifier: Modifier = Modifier,
	contentPading: PaddingValues = PaddingValues(vertical = 2.dp, horizontal = 4.dp),
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
	
	val splitText =
		if (splitRequirementsMet) text.substring(selection.start)
		else return
	
	OutlinedIconButton(
		onClick = { doSplit(splitText) },
		shape = CutCornerShape(0.dp),
		border = BorderStroke(0.dp, SolidColor(Color.Transparent)),
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
					showIndicator = false,
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
fun ArtistCreditEnButton(track: TracksItemObservable) {
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
		modifier = Modifier
			.padding(vertical = 2.dp, horizontal = 4.dp)
			.size(24.dp)
	)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTrackField(
	initialValue: TextFieldValue,
	onValueChange: (TextFieldValue) -> Unit,
	modifier: Modifier = Modifier,
	label: String? = null,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	visualTransformation: VisualTransformation = neverBlankVisualTransformation,
	singleLine: Boolean = true,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	minLines: Int = 1,
	readOnly: Boolean = false,
	fontSize: TextUnit = 14.sp,
	fontStyle: FontStyle = FontStyle.Normal,
	showIndicator: Boolean = true,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
	
	
	BasicTextField(
		value = initialValue,
		onValueChange = onValueChange,
		textStyle = TextStyle(
			fontSize = fontSize,
			fontStyle = fontStyle,
			color = Color.White,
			lineHeight = 46.sp
		),
		keyboardOptions = keyboardOptions,
		interactionSource = interactionSource,
		readOnly = readOnly,
		singleLine = singleLine,
		maxLines = maxLines,
		minLines = minLines,
		visualTransformation = visualTransformation,
		cursorBrush = SolidColor(Color.White),
		modifier = modifier
			.width(IntrinsicSize.Max)
			.indicatorLine(
				enabled = true,
				interactionSource = interactionSource,
				colors = TextFieldDefaults.colors(),
				isError = false,
				focusedIndicatorLineThickness = 1.dp,
				unfocusedIndicatorLineThickness = if (showIndicator) .5.dp else 0.dp
			)
	) { innerTextField ->
		
		TextFieldDefaults.DecorationBox(
			value = initialValue.text,
			innerTextField = innerTextField,
			enabled = true,
			singleLine = singleLine,
			visualTransformation = visualTransformation,
			interactionSource = interactionSource,
			label = label?.let { { Text(text = label) } },
			contentPadding = PaddingValues(start = 1.dp, end = 1.dp),
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
	label: String? = null,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	visualTransformation: VisualTransformation = neverBlankVisualTransformation,
	singleLine: Boolean = true,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	minLines: Int = 1,
	readOnly: Boolean = false,
	fontSize: TextUnit = 14.sp,
	fontStyle: FontStyle = FontStyle.Normal,
	showIndicator: Boolean = true,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
	
	BasicTextField(
		value = initialValue.orEmpty(),
		onValueChange = onValueChange,
		textStyle = TextStyle(
			fontSize = fontSize,
			fontStyle = fontStyle,
			color = Color.White,
			lineHeight = 46.sp
		),
		keyboardOptions = keyboardOptions,
		interactionSource = interactionSource,
		readOnly = readOnly,
		singleLine = singleLine,
		maxLines = maxLines,
		minLines = minLines,
		visualTransformation = visualTransformation,
		cursorBrush = SolidColor(Color.White),
		modifier = modifier
			.width(IntrinsicSize.Max)
			.indicatorLine(
				enabled = true,
				interactionSource = interactionSource,
				colors = TextFieldDefaults.colors(),
				isError = false,
				focusedIndicatorLineThickness = 1.dp,
				unfocusedIndicatorLineThickness = if (showIndicator) .5.dp else 0.dp
			)
	) { innerTextField ->
		
		TextFieldDefaults.DecorationBox(
			value = initialValue.orEmpty(),
			innerTextField = innerTextField,
			enabled = true,
			singleLine = singleLine,
			visualTransformation = visualTransformation,
			interactionSource = interactionSource,
			label = label?.let { { Text(text = label) } },
			contentPadding = PaddingValues(start = 1.dp, end = 1.dp),
			container = {}
		)
	}
}









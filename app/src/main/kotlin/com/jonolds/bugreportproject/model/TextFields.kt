package com.jonolds.bugreportproject.model

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonolds.bugreportproject.ui.components.neverBlankVisualTransformation
import com.jonolds.bugreportproject.ui.theme.clubhouseTypography
import com.jonolds.bugreportproject.utils.clearFocusOnKeyboardDismiss


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextJNullable(
	initialValue: String?,
	onValueChange: (String?) -> Unit,
	modifier: Modifier = Modifier,
	label: String? = null,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	containerColor: Color = Color.Transparent,
	visualTransformation: VisualTransformation = neverBlankVisualTransformation,
	singleLine: Boolean = true,
	readOnly: Boolean = false,
	fontSize: TextUnit = 16.sp,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {


	val textStyle = remember(fontSize) {
		clubhouseTypography.bodyMedium.copy(
			color = Color.White,
			fontSize = fontSize,
		)
	}

	BasicTextField(
		value = initialValue ?: "",
		onValueChange = { newValueStr ->
			onValueChange(newValueStr.ifBlank { null })
		},
		textStyle = textStyle,
		modifier = modifier.clearFocusOnKeyboardDismiss(),
		keyboardOptions = keyboardOptions,
		interactionSource = interactionSource,
		readOnly = readOnly,
		singleLine = singleLine,
		maxLines = if (singleLine) 1 else Int.MAX_VALUE,
		minLines = 1,
		visualTransformation = visualTransformation,
		cursorBrush = SolidColor(Color.White)
	) { innerTextField ->

		val colors = TextFieldDefaults.colors(
			focusedContainerColor = containerColor,
			unfocusedContainerColor = containerColor,
			disabledContainerColor = containerColor,
		)

		TextFieldDefaults.DecorationBox(
			value = initialValue ?: "",
			innerTextField = innerTextField,
			enabled = true,
			singleLine = singleLine,
			visualTransformation = visualTransformation,
			interactionSource = interactionSource,
			label = label?.let { {
				Text(text = label)
			} },
			colors = colors,
			contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
		)
	}


}
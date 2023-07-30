package com.jonolds.bugreportproject.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


val neverBlankVisualTransformation = VisualTransformation { text ->
	if(text.text.isBlank())
		TransformedText(if(text.text.isBlank()) AnnotatedString("        ") else text, object : OffsetMapping {
			override fun originalToTransformed(offset: Int): Int = 0
			override fun transformedToOriginal(offset: Int): Int = 0
			
		})
	else
		TransformedText(text, OffsetMapping.Identity)
}


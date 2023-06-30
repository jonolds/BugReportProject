package com.jonolds.bugreportproject.utils


fun <T>MutableList<T>.move(from: Int, to: Int) {
	if(from != to)
		add(kotlin.math.min(to, size-1), removeAt(from))
}


fun String.trimNullIfBlank(): String? =
	trim().ifBlank { null }

fun String?.trimNullIfBlankCompare(other: String?): Boolean =
	this?.trim()?.ifBlank { null } == other?.trim()?.ifBlank { null }


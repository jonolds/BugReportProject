@file:UseSerializers(SnapshotStateListSerializer::class)
package com.jonolds.bugreportproject.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.jonolds.bugreportproject.model.TracksItemObservable
import com.jonolds.bugreportproject.utils.move
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json

@Stable
class TracksTestVM: ViewModel() {
	
	private val _tracks = origTrackList.toMutableStateList()

	val tracks by derivedStateOf { _tracks.toImmutableList() }
	
	fun move(from: Int, to: Int) {
		_tracks.move(from, to)
	}

	
}

val json = Json {
	ignoreUnknownKeys = true
	isLenient = true
	encodeDefaults = true
}


internal val origTrackList = """[
{"position":"1","title":"I Had Too Much to Dream (Last Night)","artistCredit":"The Electric Prunes"},
{"position":"2","title":"Dirty Water","artistCredit":"The Standells"},
{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"},
{"position":"4","title":"Lies","artistCredit":"The Knickerbockers"},
{"position":"5","title":"Respect","artistCredit":"The Vagrants"},
{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"},
{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"},
{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"},
{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"},
{"position":"10","title":"Moulty","artistCredit":"The Barbarians"},
{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"},
{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"},
{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"},
{"position":"14","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"},
{"position":"15","title":"Psychotic Reaction","artistCredit":"Count Five"},
{"position":"16","title":"Hey Joe","artistCredit":"The Leaves"},
{"position":"17","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"},
{"position":"18","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"},
{"position":"19","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"},
{"position":"20","title":"Tobacco Road","artistCredit":"The Blues Magoos"},
{"position":"21","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"},
{"position":"22","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"},
{"position":"23","title":"Run, Run, Run","artistCredit":"The Third Rail"},
{"position":"24","title":"My World Fell Down","artistCredit":"Sagittarius"},
{"position":"25","title":"Open My Eyes","artistCredit":"Nazz"},
{"position":"26","title":"Farmer John","artistCredit":"The Premiers"},
{"position":"27","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}]""".trimIndent()
	.let { json.decodeFromString<List<TracksItemObservable>>(it) }
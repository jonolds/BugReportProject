@file:UseSerializers(SnapshotStateListSerializer::class)
@file:Suppress("unused")

package com.jonolds.bugreportproject.tests.trackstest

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.jonolds.bugreportproject.model.TracksItemObservable
import com.jonolds.bugreportproject.utils.json
import com.jonolds.bugreportproject.utils.move
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.Json

@Stable
class TracksTestVM: ViewModel() {
	
	private val _tracks: MutableList<TracksItemObservable> = trackList79.toMutableStateList()
	val tracks: List<TracksItemObservable> = _tracks

	
	fun move(from: Int, to: Int) {
		_tracks.move(from, to)
	}
	
	fun removeTrack(key: String) {
		val track = _tracks.first { it.position == key }
		_tracks.remove(track)
	}
	
	fun removeByIndex(i: Int) {
		_tracks.removeAt(i)
	}

	
}


internal val trackList3 by lazy {
	"""[
{"position":"1","title":"Too Much to Dream","artistCredit":"The Electric Prunes"}
,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
]""".trimIndent()
		.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}

internal val trackList5 by lazy { """[
{"position":"1","title":"Too Much to Dream","artistCredit":"The Electric Prunes"}
,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"4","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
]""".trimIndent()
		.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}

internal val trackList9 by lazy {
	"""[
{"position":"1","title":"Too Much to Dream","artistCredit":"The Electric Prunes"}
,{"position":"2","title":"Dirty Water","artistCredit":""}
,{"position":"3","title":"Night Time","artistCredit":null}
,{"position":"4","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
]""".trimIndent()
		.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}

internal val trackList13 by lazy {
	"""[
{"position":"1","title":"Too Much to Dream","artistCredit":"The Electric Prunes"}
,{"position":"2","title":"Dirty Water","artistCredit":""}
,{"position":"3","title":"Night Time","artistCredit":null}
,{"position":"4","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"10","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"}
]""".trimIndent()
		.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}

internal val trackList27 =
	"""[
{"position":"1","title":"Too Much to Dream","artistCredit":"The Electric Prunes"}
,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"4","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"10","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"14","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"15","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"16","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"17","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"18","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"19","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"20","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"21","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"22","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"23","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"24","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"25","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"26","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"27","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
]""".trimIndent()
		.let { json.decodeFromString<List<TracksItemObservable>>(it) }


//internal val trackList27 by lazy {
//	"""[
//{"position":"1","title":"Too Much to Dream","artistCredit":"The Electric Prunes"}
//,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
//,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
//,{"position":"4","title":"Lies","artistCredit":"The Knickerbockers"}
//,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
//,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
//,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
//,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
//,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
//,{"position":"10","title":"Moulty","artistCredit":"The Barbarians"}
//,{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"}
//,{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"}
//,{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"}
//,{"position":"14","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
//,{"position":"15","title":"Psychotic Reaction","artistCredit":"Count Five"}
//,{"position":"16","title":"Hey Joe","artistCredit":"The Leaves"}
//,{"position":"17","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
//,{"position":"18","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
//,{"position":"19","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
//,{"position":"20","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
//,{"position":"21","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
//,{"position":"22","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
//,{"position":"23","title":"Run, Run, Run","artistCredit":"The Third Rail"}
//,{"position":"24","title":"My World Fell Down","artistCredit":"Sagittarius"}
//,{"position":"25","title":"Open My Eyes","artistCredit":"Nazz"}
//,{"position":"26","title":"Farmer John","artistCredit":"The Premiers"}
//,{"position":"27","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
//]""".trimIndent()
//		.let { json.decodeFromString<List<TracksItemObservable>>(it) }
//}

internal val trackList40 by lazy { """[
{"position":"1","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"4","title":"I Had Too Much to Dream (Last Night)","artistCredit":"The Electric Prunes"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"10","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"14","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"15","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"16","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"17","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"18","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"19","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"20","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"21","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"22","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"23","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"24","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"25","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"26","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"27","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
,{"position":"28","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"29","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"30","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"31","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"32","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"33","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"34","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"35","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"36","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"37","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"38","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"39","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"40","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
]""".trimIndent()
	.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}

internal val trackList54 by lazy { """[
{"position":"1","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"4","title":"I Had Too Much to Dream (Last Night)","artistCredit":"The Electric Prunes"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"10","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"14","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"15","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"16","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"17","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"18","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"19","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"20","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"21","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"22","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"23","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"24","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"25","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"26","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"27","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
,{"position":"28","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"29","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"30","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"31","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"32","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"33","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"34","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"35","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"36","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"37","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"38","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"39","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"40","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"41","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"42","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"43","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"44","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"45","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"46","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"47","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"48","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"49","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"50","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"51","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"52","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"53","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
,{"position":"54","title":"Dirty Water","artistCredit":"The Standells"}
]""".trimIndent()
	.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}


internal val trackList79 by lazy { """[
{"position":"1","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"2","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"3","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"4","title":"I Had Too Much to Dream (Last Night)","artistCredit":"The Electric Prunes"}
,{"position":"5","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"6","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"7","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"8","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"9","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"10","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"11","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"12","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"13","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"14","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"15","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"16","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"17","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"18","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"19","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"20","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"21","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"22","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"23","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"24","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"25","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"26","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"27","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
,{"position":"28","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"29","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"30","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"31","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"32","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"33","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"34","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"35","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"36","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"37","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"38","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"39","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"40","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"41","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"42","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"43","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"44","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"45","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"46","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"47","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"48","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"49","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"50","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"51","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"52","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"53","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
,{"position":"54","title":"Dirty Water","artistCredit":"The Standells"}
,{"position":"55","title":"Night Time","artistCredit":"The Strangeloves"}
,{"position":"56","title":"Lies","artistCredit":"The Knickerbockers"}
,{"position":"57","title":"Respect","artistCredit":"The Vagrants"}
,{"position":"58","title":"A Public Execution","artistCredit":"Mouse and the Traps"}
,{"position":"59","title":"No Time Like the Right Time","artistCredit":"The Blues Project"}
,{"position":"60","title":"Oh Yeah","artistCredit":"The Shadows of Knight"}
,{"position":"61","title":"Pushin' Too Hard","artistCredit":"The Seeds"}
,{"position":"62","title":"Moulty","artistCredit":"The Barbarians"}
,{"position":"63","title":"Don't Look Back","artistCredit":"The Remains"}
,{"position":"64","title":"An Invitation to Cry","artistCredit":"The Magicians"}
,{"position":"65","title":"Liar, Liar","artistCredit":"The Castaways"}
,{"position":"66","title":"You're Gonna Miss Me","artistCredit":"13th Floor Elevators"}
,{"position":"67","title":"Psychotic Reaction","artistCredit":"Count Five"}
,{"position":"68","title":"Hey Joe","artistCredit":"The Leaves"}
,{"position":"69","title":"Romeo and Juliet","artistCredit":"Michael and the Messengers"}
,{"position":"70","title":"Sugar and Spice","artistCredit":"The Cryan' Shames"}
,{"position":"71","title":"Baby Please Don't Go","artistCredit":"The Amboy Dukes"}
,{"position":"72","title":"Tobacco Road","artistCredit":"The Blues Magoos"}
,{"position":"73","title":"Let's Talk About Girls","artistCredit":"The Chocolate Watch Band"}
,{"position":"74","title":"Sit Down, I Think I Love You","artistCredit":"The Mojo Men"}
,{"position":"75","title":"Run, Run, Run","artistCredit":"The Third Rail"}
,{"position":"76","title":"My World Fell Down","artistCredit":"Sagittarius"}
,{"position":"77","title":"Open My Eyes","artistCredit":"Nazz"}
,{"position":"78","title":"Farmer John","artistCredit":"The Premiers"}
,{"position":"79","title":"It's A‐Happening","artistCredit":"The Magic Mushrooms"}
]""".trimIndent()
		.let { json.decodeFromString<List<TracksItemObservable>>(it) }
}
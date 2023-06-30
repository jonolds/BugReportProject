@file:UseSerializers(SnapshotStateListSerializer::class, MutableStateSerializer::class, StateSerializer::class)
package com.jonolds.bugreportproject.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jonolds.bugreportproject.utils.serializers.MutableStateSerializer
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializer
import com.jonolds.bugreportproject.utils.serializers.StateSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

@Stable @Serializable(with = TracksItemObservableSerializer::class)
class TracksItemObservable(
	position: String? = null,
	title: String,
	artistCredit: String? = null
): AbstractTracksItem() {


	override var position by mutableStateOf(position)
	override var title by mutableStateOf(title)
	override var artistCredit by mutableStateOf(artistCredit)

	
	val uuid: String = UUID.randomUUID().toString()
	
	var artistCreditEn by mutableStateOf(!artistCredit.isNullOrBlank())

	override fun toString(): String = "TracksItemObservable(position=${position}, title=${title}, artistCredit=${artistCredit})"

}

@Stable @OptIn(ExperimentalSerializationApi::class)
class TracksItemObservableSerializer : KSerializer<TracksItemObservable> {
	val delegatedSerializer = AbstractTracksItem.serializer()
	override val descriptor: SerialDescriptor = SerialDescriptor("TracksItemObservable", delegatedSerializer.descriptor)

	override fun deserialize(decoder: Decoder): TracksItemObservable {
		val jsonObject = (decoder as JsonDecoder).decodeJsonElement().jsonObject

		return TracksItemObservable(
			position = jsonObject.stringOrNull("position"),
			title = jsonObject.string("title"),
			artistCredit = jsonObject.stringOrNull("artistCredit"),
		)
	}

	override fun serialize(encoder: Encoder, value: TracksItemObservable) =
		delegatedSerializer.serialize(encoder, value as AbstractTracksItem)
}

fun JsonObject.string(key: String): String = this[key]!!.jsonPrimitive.content
fun JsonObject.stringOrNull(key: String): String? = this[key]?.jsonPrimitive?.contentOrNull



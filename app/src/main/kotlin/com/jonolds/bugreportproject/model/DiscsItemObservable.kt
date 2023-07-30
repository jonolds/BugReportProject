@file:UseSerializers(SnapshotStateListSerializer::class, MutableStateSerializer::class,
	StateSerializer::class, SnapshotStateListSerializerEmptyIfNull::class)
package com.jonolds.bugreportproject.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.clubhouse.AbstractDiscsItem
import com.jonolds.bugreportproject.utils.serializers.MutableStateSerializer
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializer
import com.jonolds.bugreportproject.utils.serializers.SnapshotStateListSerializerEmptyIfNull
import com.jonolds.bugreportproject.utils.serializers.StateSerializer
import com.jonolds.stringOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.util.Collections

@Stable
@Serializable(with = DiscItemObservableSerializer::class)
class DiscsItemObservable(
	discTitle: String? = null,

	@Serializable(with = SnapshotStateListSerializer::class)
	override val tracks: SnapshotStateList<TracksItemObservable> = SnapshotStateList()
): AbstractDiscsItem() {


	override var discTitle by mutableStateOf(discTitle)


	constructor(discsItem: AbstractDiscsItem): this(
		discTitle = discsItem.discTitle,
		tracks = discsItem.tracks.map { TracksItemObservable(it) }.toMutableStateList()
	)

}


@Stable @OptIn(ExperimentalSerializationApi::class)
class DiscItemObservableSerializer : KSerializer<DiscsItemObservable> {
	val delegatedSerializer = AbstractDiscsItem.serializer()
	override val descriptor: SerialDescriptor = SerialDescriptor("DiscsItemObservable", delegatedSerializer.descriptor)

	override fun deserialize(decoder: Decoder): DiscsItemObservable {
		val jsonObject = (decoder as JsonDecoder).decodeJsonElement().jsonObject

		return DiscsItemObservable(
			discTitle = jsonObject.stringOrNull("discTitle"),
			tracks = decoder.json.decodeFromJsonElement(SnapshotStateListSerializer(TracksItemObservable.serializer()), jsonObject["tracks"]!!.jsonArray),
		)
	}

	override fun serialize(encoder: Encoder, value: DiscsItemObservable) =
		delegatedSerializer.serialize(encoder, value as AbstractDiscsItem)
}
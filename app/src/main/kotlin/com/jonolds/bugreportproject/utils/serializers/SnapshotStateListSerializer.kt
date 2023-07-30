package com.jonolds.bugreportproject.utils.serializers

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder

@OptIn(ExperimentalSerializationApi::class)
@Stable @Serializer(SnapshotStateList::class)
class SnapshotStateListSerializer<T>(dataSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> {
	val listSerializer = ListSerializer(dataSerializer)
	override val descriptor: SerialDescriptor = listSerializer.descriptor

	override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) =
		listSerializer.serialize(encoder, value)

	override fun deserialize(decoder: Decoder): SnapshotStateList<T> = with(decoder as JsonDecoder) {
		val list = listSerializer.deserialize(decoder)
		SnapshotStateList<T>().also { it.addAll(list) }
	}
}



@OptIn(ExperimentalSerializationApi::class)
@Stable
class SnapshotStateListSerializerEmptyIfNull<T>(dataSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> {
	val listSerializer = ListSerializer(dataSerializer).nullable
	override val descriptor: SerialDescriptor = listSerializer.descriptor

	override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
		if(value.isEmpty())
			listSerializer.serialize(encoder, null)
		else
			listSerializer.serialize(encoder, value)
	}

	override fun deserialize(decoder: Decoder): SnapshotStateList<T> =
		SnapshotStateList<T>().also {
			listSerializer.deserialize(decoder)?.let { list -> it.addAll(list) }
		}
}
package com.jonolds.bugreportproject.utils.serializers

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder


@Stable
class StateSerializer<T: Any?>(val dataSerializer: KSerializer<T>) : KSerializer<State<T>> {

	override val descriptor: SerialDescriptor = dataSerializer.descriptor

	override fun serialize(encoder: Encoder, value: State<T>) =
		dataSerializer.serialize(encoder, value.value)

	override fun deserialize(decoder: Decoder): State<T> = with(decoder as JsonDecoder) {
		val list = dataSerializer.deserialize(decoder)
		mutableStateOf(list)
	}
}

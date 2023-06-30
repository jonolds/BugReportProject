package com.jonolds.bugreportproject.model

import com.jonolds.bugreportproject.utils.trimNullIfBlank
import com.jonolds.bugreportproject.utils.trimNullIfBlankCompare
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


@Serializable(with = AbstractTracksItemSerializer::class)
abstract class AbstractTracksItem {
	abstract val position: String?
	abstract val title: String
	abstract val artistCredit: String?
	
	final override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is AbstractTracksItem) return false
		if (javaClass != other.javaClass) return false
		
		return dataEquals(other)
	}
	
	final override fun hashCode(): Int {
		var result = position?.trimNullIfBlank()?.hashCode() ?: 0
		result = 31 * result + title.trimNullIfBlank().hashCode()
		result = 31 * result + (artistCredit?.trimNullIfBlank()?.hashCode() ?: 0)
		return result
	}
	
	fun dataEquals(other: AbstractTracksItem): Boolean {
		if (!position.trimNullIfBlankCompare(other.position)) return false
		if (!title.trimNullIfBlankCompare(other.title)) return false
		return artistCredit.trimNullIfBlankCompare(other.artistCredit)
	}
	
	override fun toString(): String =
		"AbstractTracksItem(position=$position, title='$title', artistCredit=$artistCredit)"
}


@Serializable
data class TracksItem(
	override val position: String? = null,
	override val title: String,
	override val artistCredit: String? = null,
): AbstractTracksItem() {
	
	override fun toString(): String =
		"TracksItem(position=$position, title='$title', artistCredit=$artistCredit)"
}




@OptIn(ExperimentalSerializationApi::class)
class AbstractTracksItemSerializer : KSerializer<AbstractTracksItem> {
	
	private val delegatedSerializer = TracksItem.serializer()
	override val descriptor: SerialDescriptor = SerialDescriptor("AbstractTracksItem", delegatedSerializer.descriptor)
	
	override fun deserialize(decoder: Decoder): AbstractTracksItem =
		delegatedSerializer.deserialize(decoder)
	
	override fun serialize(encoder: Encoder, value: AbstractTracksItem) {
		check(encoder is JsonEncoder)
		
		val jsonObj = buildJsonObject {
			put("position", value.position)
			put("title", value.title)
			put("artistCredit", value.artistCredit)
		}
		encoder.encodeJsonElement(jsonObj)
	}
}
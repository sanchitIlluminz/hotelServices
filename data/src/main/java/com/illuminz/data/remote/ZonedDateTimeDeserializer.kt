package com.illuminz.data.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.lang.reflect.Type

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZonedDateTime? {
        if (json != null) {
            try {
                val utcDateString = json.asString
                return Instant.parse(utcDateString).atZone(ZoneId.systemDefault())
            } catch (exception: Exception) {
                Timber.w(exception)
            }
        }
        return null
    }
}
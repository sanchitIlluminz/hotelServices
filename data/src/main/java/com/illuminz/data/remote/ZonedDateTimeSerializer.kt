package com.illuminz.data.remote

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.ZonedDateTime
import java.lang.reflect.Type

class ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime?> {
    override fun serialize(
        zonedDateTime: ZonedDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        zonedDateTime ?: return null
        return JsonPrimitive(zonedDateTime.toInstant().toString())
    }
}
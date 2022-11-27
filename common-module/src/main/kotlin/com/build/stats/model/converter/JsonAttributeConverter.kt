package com.build.stats.model.converter

import com.fasterxml.jackson.databind.ObjectMapper
import liquibase.pro.packaged.T
import java.lang.reflect.ParameterizedType
import javax.persistence.AttributeConverter


abstract class JsonAttributeConverter<T : Any> : AttributeConverter<T, String> {
    private val objectMapper = ObjectMapper()

    @Suppress("unchecked_cast")
    private val objectCls = (javaClass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0] as Class<T>

    override fun convertToDatabaseColumn(attribute: T?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): T? {
        return dbData?.let { objectMapper.readValue(it, objectCls) }
    }
}
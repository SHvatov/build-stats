package com.build.stats.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * Note: needs to be annotated with @Suppress("JpaAttributeTypeInspection") due to some bug in Idea.
 * */
class RequiredProperty<T : Any, V : Any>(
    private val backingProperty: KMutableProperty0<V?>,
    private val mandatory: Boolean = false,
    defaultValueProvider: (() -> V)? = null
) : ReadWriteProperty<T, V> {
    init {
        require(mandatory xor (defaultValueProvider != null)) {
            "Field must be either mandatory or provide \"defaultValueProvider\""
        }
    }

    private val defaultValue: V by lazy {
        if (mandatory) {
            throw IllegalStateException(
                "Access default value provider" +
                        " while property is marked as mandatory"
            )
        }
        defaultValueProvider!!()
    }

    /**
     * Returns either the value, which is stored in [backingProperty]
     * or one, generated using [defaultValue].
     */
    override fun getValue(thisRef: T, property: KProperty<*>): V {
        return if (mandatory) {
            backingProperty.get()!!
        } else {
            backingProperty.get() ?: defaultValue
        }
    }

    /**
     * Sets the value of the backing field to [value].
     */
    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        backingProperty.set(value)
    }
}
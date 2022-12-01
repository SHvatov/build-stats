package com.build.stats.vo.filter

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class UiFilterData(
    @JsonProperty("page") val page: Int? = null,
    @JsonProperty("pageSize") val pageSize: Int? = null,
    @JsonProperty("params") val params: Map<String, Any?>? = null,
) {
    init {
        require(
            page == null && pageSize == null
                    || page != null && pageSize != null
        )

        if (page != null && pageSize != null) {
            require(page > 0 && pageSize > 0)
        }
    }

    @JsonIgnore
    val isPaged: Boolean = page != null && pageSize != null

    @JsonIgnore
    @Suppress("unchecked_cast")
    inline fun <reified T> getAttribute(attrCode: String): T? {
        val param = params?.get(attrCode)
        return if (param is T) {
            return param
        } else null
    }
}


package com.farshidabz.skysense.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ConditionDTO(
    @SerialName("text")
    val text: String? = null,
    @SerialName("icon")
    val icon: String? = null,
    @SerialName("code")
    val code: Int? = null
)

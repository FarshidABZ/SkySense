package com.farshidabz.skysense.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    @SerialName("name")
    val name: String? = null,
    @SerialName("country")
    val country: String? = null,
)

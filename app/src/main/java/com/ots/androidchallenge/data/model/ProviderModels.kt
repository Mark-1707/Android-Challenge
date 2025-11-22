package com.ots.androidchallenge.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomTextValue(
    val value: String,
    val length: Int,
    val created: String
)

@Serializable
data class ProviderResponse(
    @SerialName("randomText")
    val randomText: RandomTextValue
)

package com.mambo.mywaterpay.models

import kotlinx.serialization.Serializable

@Serializable
data class WaterMeter(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)

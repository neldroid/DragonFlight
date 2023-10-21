package com.interview.dragonflights.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["from", "to"])
data class Currency(
    val from: String,
    val to: String,
    val exchangeRate: Double
)

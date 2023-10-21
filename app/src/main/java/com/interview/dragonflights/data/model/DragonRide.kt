package com.interview.dragonflights.data.model

data class DragonRide(
    val inbound: Flight,
    val outbound: Flight,
    var price: Double,
    var currency: String
) {

    fun toKey() = "${inbound.origin} - ${inbound.destination}"

}
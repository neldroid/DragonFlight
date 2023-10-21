package com.interview.dragonflights.domain

import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.data.repository.CurrencyRepository
import com.interview.dragonflights.data.repository.DragonRideRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import javax.inject.Inject

class GetDragonRidesHomeUseCase @Inject constructor(
    private val dragonRidesRepository: DragonRideRepository,
    private val currencyRepository: CurrencyRepository
) {

    var result: Map<String, List<DragonRide>> = mutableMapOf()

    suspend fun fetchFlights() =
        withContext(Dispatchers.IO) {
            var currencyExchanges = emptyList<String>()
            var convertedRides = emptyList<DragonRide>()

            dragonRidesRepository.fetchDragonRides()
                .onEach { rides ->
                    currencyExchanges = rides.filter { it.currency != "EUR" }.map { it.currency }
                }
                .combine(
                    currencyRepository.fetchLastCurrencyExchange(
                        currencyExchanges,
                        "EUR"
                    )
                ) { dragonRides, currencies ->
                    convertedRides = dragonRides.map { ride ->
                        if (ride.currency == "EUR") {
                            ride
                        } else {
                            val rate = currencies.firstOrNull()?.exchangeRate ?: 1.0
                            val convertedPrice =
                                (ride.price / rate).toBigDecimal().setScale(2, RoundingMode.UP)
                                    .toDouble()
                            ride.copy(currency = "EUR", price = convertedPrice)
                        }
                    }.sortedBy { it.price }
                }
                .collect {
                    result = convertedRides.groupBy { it.toKey() }
                    result.values.map {
                        it.sortedBy { it.price }
                    }
                }

            return@withContext result
        }

    fun fetchFlights(fromPrice: Double = 0.0, toPrice: Double = Double.MAX_VALUE) =
        result.flatMap { it.value }.filter { it.price in fromPrice..toPrice }.groupBy { it.toKey() }
}
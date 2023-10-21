package com.interview.dragonflights.data.repository

import com.interview.dragonflights.data.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    /**
     * Fetches the last currency exchange rate for the specified currency pair.
     *
     * @param from The currency code for the currency to convert from.
     * @param to The currency code for the currency to convert to.
     * @return A [Flow] of [Currency] that emits the latest currency exchange rate for the specified currency pair.
     */
    suspend fun fetchLastCurrencyExchange(from: List<String>, to: String): Flow<List<Currency>>

}
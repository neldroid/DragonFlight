package com.interview.dragonflights.data.repository

import com.interview.dragonflights.data.model.Currency
import com.interview.dragonflights.data.model.CurrencyExchangeResponse
import com.interview.dragonflights.data.source.CurrencyExchangeLocalDataSource
import com.interview.dragonflights.data.source.CurrencyExchangeRemoteDataSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class DefaultCurrencyRepository @Inject constructor(
    private val currencyExchangeRemoteDataSource: CurrencyExchangeRemoteDataSource,
    private val currencyExchangeLocalDataSource: CurrencyExchangeLocalDataSource
) : CurrencyRepository {

    override suspend fun fetchLastCurrencyExchange(from: List<String>, to: String): Flow<List<Currency>> {
        return flow {
            val currencies = mutableListOf<Currency>()
            from.forEach { currency ->
                val localCurrency = currencyExchangeLocalDataSource.getCurrencyExchangeResponse(currency, to).singleOrNull()
                if (localCurrency != null) {
                    currencies.add(localCurrency)
                } else {
                    val currencyDataExchange = currencyExchangeRemoteDataSource.getCurrencyExchangeResponse(currency, to).singleOrNull()
                    currencyDataExchange?.let {
                        val currency = Currency(it.currency, to, it.exchangeRate)
                        currencyExchangeLocalDataSource.saveCurrency(currency)
                        currencies.add(currency)
                    }
                }
            }
            emit(currencies)
        }
    }

}
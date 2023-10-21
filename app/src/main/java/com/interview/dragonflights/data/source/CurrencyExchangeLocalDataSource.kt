package com.interview.dragonflights.data.source

import com.interview.dragonflights.data.dao.CurrencyDAO
import com.interview.dragonflights.data.model.Currency
import kotlinx.coroutines.flow.flow

import javax.inject.Inject


class CurrencyExchangeLocalDataSource @Inject constructor(
    private val currencyDAO: CurrencyDAO
) {

    fun getCurrencyExchangeResponse(from: String, to: String) =
        currencyDAO.getCurrencyExchange(from, to)

    fun saveCurrency(currency: Currency){
        currencyDAO.insert(currency)
    }
}
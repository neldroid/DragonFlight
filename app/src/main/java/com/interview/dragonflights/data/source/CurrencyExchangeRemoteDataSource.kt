package com.interview.dragonflights.data.source

import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit

import javax.inject.Inject


class CurrencyExchangeRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit
) {

    fun getCurrencyExchangeResponse(from: String, to: String) = flow {
        val currencyExchangeService = retrofit.create(CurrencyExchangeService::class.java)

        emit(currencyExchangeService.getCurrency(from, to))
    }
}
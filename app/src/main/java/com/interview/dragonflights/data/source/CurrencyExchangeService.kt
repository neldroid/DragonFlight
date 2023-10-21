package com.interview.dragonflights.data.source

import com.interview.dragonflights.data.model.CurrencyExchangeResponse
import com.interview.dragonflights.data.model.DragonRide
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyExchangeService {

    @GET ("bin/7f11d0fd-fc90-4077-938e-2f1e418effee")
    suspend fun getCurrency(@Query("from") from:String, @Query("to") to:String): CurrencyExchangeResponse

}
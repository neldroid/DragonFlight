package com.interview.dragonflights.data.source

import com.interview.dragonflights.data.model.DragonRide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import com.interview.dragonflights.ui.utils.Result

import javax.inject.Inject


class FlightsDataSource @Inject constructor(
    private val retrofit: Retrofit
) {

    val dragonRides: Flow<List<DragonRide>> = flow {
        val dragonRidesService = retrofit.create(DragonRidesService::class.java)

        val dragonRides = dragonRidesService.getDragonRides()
        emit(dragonRides)

    }
}
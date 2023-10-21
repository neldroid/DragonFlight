package com.interview.dragonflights.data.repository

import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.data.source.FlightsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultDragonRidesRepository @Inject constructor(
    private val dragonRidesDataSource: FlightsDataSource
): DragonRideRepository {

    override suspend fun fetchDragonRides(): Flow<List<DragonRide>> = dragonRidesDataSource.dragonRides

}
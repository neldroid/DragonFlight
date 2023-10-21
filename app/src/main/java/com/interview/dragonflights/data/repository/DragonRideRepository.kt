package com.interview.dragonflights.data.repository

import com.interview.dragonflights.data.model.DragonRide
import kotlinx.coroutines.flow.Flow

interface DragonRideRepository {

    suspend fun fetchDragonRides(): Flow<List<DragonRide>>

}
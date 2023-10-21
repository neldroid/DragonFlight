package com.interview.dragonflights.data.source

import com.interview.dragonflights.data.model.DragonRide
import retrofit2.http.GET

interface DragonRidesService {

    @GET ("bin/1b5a0333-8baa-48b3-a2b6-389c11f46b47")
    suspend fun getDragonRides(): List<DragonRide>

}
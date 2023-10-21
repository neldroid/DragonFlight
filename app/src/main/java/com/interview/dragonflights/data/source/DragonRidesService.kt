package com.interview.dragonflights.data.source

import com.interview.dragonflights.data.model.DragonRide
import retrofit2.http.GET

interface DragonRidesService {

    @GET ("bin/a808dc89-004d-4a1f-b4f1-e9b9f25267ff")
    suspend fun getDragonRides(): List<DragonRide>

}
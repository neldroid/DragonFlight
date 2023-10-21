package com.interview.dragonflights

import com.interview.dragonflights.data.model.Flight

object TestMockUtil {

    fun createDummyFlight(
        airline: String = "DummyAirlineName",
        origin: String = "A",
        destination: String = "B"
    ) =
        Flight(
            airline = airline,
            airlineImage = "https://example.com/airline.png",
            arrivalDate = "2022-04-01",
            arrivalTime = "15:30",
            departureDate = "2022-04-01",
            departureTime = "12:00",
            destination = destination,
            origin = origin
        )

}
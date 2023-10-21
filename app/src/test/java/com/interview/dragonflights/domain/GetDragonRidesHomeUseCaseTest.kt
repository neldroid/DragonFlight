package com.interview.dragonflights.domain

import com.interview.dragonflights.TestMockUtil.createDummyFlight
import com.interview.dragonflights.data.model.Currency
import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.data.repository.CurrencyRepository
import com.interview.dragonflights.data.repository.DragonRideRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever


class GetDragonRidesHomeUseCaseTest {

    private val dragonRidesRepository = mock(DragonRideRepository::class.java)
    private val currencyRepository = mock(CurrencyRepository::class.java)
    private val dummyFlightAB = createDummyFlight()
    private val dummyFlightCD = createDummyFlight(origin = "C", destination = "D")
    private val dragonRides = listOf(
        DragonRide(dummyFlightAB, dummyFlightAB, 350.0, "USD"),
        DragonRide(dummyFlightAB, dummyFlightAB, 250.0, "USD"),
        DragonRide(dummyFlightCD, dummyFlightCD, 300.0, "EUR")
    )
    private val currencyExchanges = listOf(
        Currency("USD", "EUR", 0.86)
    )

    private lateinit var useCase: GetDragonRidesHomeUseCase

    @Before
    fun setup() = runBlocking {
        whenever(dragonRidesRepository.fetchDragonRides()).thenReturn(flowOf(dragonRides))
        whenever(currencyRepository.fetchLastCurrencyExchange(emptyList(), "EUR")).thenReturn(
            flowOf(currencyExchanges)
        )

        useCase = GetDragonRidesHomeUseCase(dragonRidesRepository, currencyRepository)
    }

    @Test
    fun `fetchFlights returns converted dragon rides grouped by origin and destination and order by price`() =
        runTest {
            // Expected result: Map("A - B" to 2 dummy Dragon rides, "C - D" to 1 dummy Dragon rides
            val result = useCase.fetchFlights()

            // Assert
            assertEquals(2, result.size)
            assertEquals(
                listOf(
                    DragonRide(dummyFlightAB, dummyFlightAB, 290.7, "EUR"),
                    DragonRide(dummyFlightAB, dummyFlightAB, 406.98, "EUR")
                ), result["A - B"]
            )
            assertEquals(
                listOf(
                    DragonRide(dummyFlightCD, dummyFlightCD, 300.0, "EUR")
                ), result["C - D"]
            )
        }

    @Test
    fun `fetchFlights with price range returns converted dragon rides within the specified range`() =
        runTest {
            useCase.fetchFlights()
            val result = useCase.fetchFlights(fromPrice = 300.0, toPrice = 400.0)

            // Assert
            assertEquals(1, result.size)
            assertEquals(
                listOf(
                    DragonRide(dummyFlightCD, dummyFlightCD, 300.0, "EUR")
                ), result["C - D"]
            )

        }

}

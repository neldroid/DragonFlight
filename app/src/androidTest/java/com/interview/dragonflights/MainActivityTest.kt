package com.interview.dragonflights

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.data.model.Flight
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSingleDragonRideItem_thenShowGeniusDeal() {
        composeTestRule.activity.setContent {
            DragonRideItem(
                dragonRide = DragonRide(
                    inbound = createDummyFlight(),
                    outbound = createDummyFlight(),
                    currency = "EUR",
                    price = 2000.0
                ),
                isBestOption = true,
            )
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.best_option))
            .assertIsDisplayed()
    }

    @Test
    fun testDragonRidesItemCreation_thenNonShowGeniusDeal() {
        composeTestRule.activity.setContent {
            DragonRidesItem(
                dragonRideKey = "A - B", dragonRides = listOf(
                    DragonRide(
                        inbound = createDummyFlight(),
                        outbound = createDummyFlight(),
                        currency = "EUR",
                        price = 2000.0
                    )
                )
            )
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.best_option))
            .assertDoesNotExist()
    }


    @Test
    fun testLoading_thenComponentShow() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.loading_rides))
            .assertIsDisplayed()
    }

    @Test
    fun testLoadRides_thenFilterAndListAreShow() {
        composeTestRule.waitForIdle()
        composeTestRule.apply {
            onNodeWithText(composeTestRule.activity.getString(R.string.filter_title))
                .assertIsDisplayed()

            onNodeWithTag("DRAGON_RIDES")
                .assertIsDisplayed()
        }
    }

    @Test
    fun testDragonRidesListCorrectElements() {
        composeTestRule.activity.setContent {
            MainDragonRides(
                flights = createDummyDragonRides(),
                onSetFilter = { min, max -> },
                filterRange = 0f..1f
            )
        }

        composeTestRule.apply {
            onNodeWithTag("DRAGON_RIDES")
                .onChildren()
                .onFirst()
                .assert(hasAnyChild(hasText("A - B")))

            onNodeWithTag("DRAGON_RIDES")
                .onChildren()
                .onLast()
                .assert(hasAnyChild(hasText("G - H")))
        }
    }
}


private fun createDummyDragonRides(): Map<String, List<DragonRide>> =
    mapOf(
        "A - B" to listOf(createDummyDragonRide()),
        "C - D" to listOf(createDummyDragonRide()),
        "E - F" to listOf(createDummyDragonRide()),
        "G - H" to listOf(createDummyDragonRide())
    )

private fun createDummyDragonRide() =
    DragonRide(createDummyFlight(), createDummyFlight(), 100.0, "EUR")

private fun createDummyFlight() =
    Flight(
        airline = "airline",
        airlineImage = "https://example.com/airline.png",
        arrivalDate = "2022-04-01",
        arrivalTime = "15:30",
        departureDate = "2022-04-01",
        departureTime = "12:00",
        origin = "A",
        destination = "B"
    )
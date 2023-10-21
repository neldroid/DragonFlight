package com.interview.dragonflights.data.model

import com.interview.dragonflights.TestMockUtil.createDummyFlight
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DragonRideTest {

    @Test
    fun testToKey() {
        val inbound = createDummyFlight()
        val outbound = createDummyFlight()
        val dragonRide = DragonRide(inbound, outbound, 500.0, "EUR")

        val result = dragonRide.toKey()

        assertEquals("A - B", result)
    }

}
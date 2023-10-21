package com.interview.dragonflights

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.data.model.Flight
import com.interview.dragonflights.ui.FlightsViewModel
import com.interview.dragonflights.ui.theme.DragonFlightsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragonFlightsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeDragonRides()
                }
            }
        }
    }
}

@Composable
fun HomeDragonRides() {
    val flightsViewModel: FlightsViewModel = viewModel()
    val flights by flightsViewModel.flights.observeAsState(mutableMapOf())

    val filterMinPrice by flightsViewModel.minPrice.observeAsState(0.0)
    val filterMaxPrice by flightsViewModel.maxPrice.observeAsState(0.0)

    val loadingRides = flights.isEmpty()

    if (loadingRides){
        Row (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            CircularProgressIndicator()
            Text(text = stringResource(id = R.string.loading_rides))
        }
    } else {
        MainDragonRides(
            flights = flights,
            filterRange = filterMinPrice.toFloat()..filterMaxPrice.toFloat(),
            onSetFilter = { min, max ->
                flightsViewModel.filterFlightsByPrice(min, max)
            },
        )
    }

}

@Composable
fun MainDragonRides(
    flights: Map<String, List<DragonRide>>,
    onSetFilter: (Double, Double) -> Unit,
    filterRange: ClosedFloatingPointRange<Float>
) {
    Column(modifier = Modifier.fillMaxHeight()) {
        if (filterRange.start >= 0f && filterRange.endInclusive > 0f) {
            FilterComponent(
                sliderRange = filterRange,
                onThumbValueChange = { minValue, maxValue ->
                    onSetFilter(minValue.toDouble(), maxValue.toDouble())
                })
        }
        LazyColumn(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .testTag("DRAGON_RIDES")
        ) {
            for ((key, value) in flights) {
                item {
                    DragonRidesItem(dragonRideKey = key, dragonRides = value)
                }
            }
        }
    }
}

@Composable
fun DragonRidesItem(dragonRideKey: String, dragonRides: List<DragonRide>) {
    Card(elevation = 4.dp) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = dragonRideKey,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )

            dragonRides.forEachIndexed { index, dragonRide ->
                DragonRideItem(
                    dragonRide = dragonRide,
                    isBestOption = (index == 0 && dragonRides.size > 1)
                )
            }
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun DragonRideItem(
    dragonRide: DragonRide,
    isBestOption: Boolean
) {
    val borderModifier =
        if (isBestOption) Modifier.border(2.dp, MaterialTheme.colors.primary) else Modifier
    if (isBestOption) {
        Text(
            text = stringResource(id = R.string.best_option),
            color = Color.White,
            modifier = Modifier
                .background(color = MaterialTheme.colors.primary)
                .padding(start = 8.dp, end = 8.dp)
        )
    }
    Row(
        modifier = borderModifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier.weight(0.6f)
        ) {
            FlightTimeInformationRow(
                title = stringResource(id = R.string.departure_title),
                flight = dragonRide.outbound
            )

            FlightTimeInformationRow(
                title = stringResource(id = R.string.return_title),
                flight = dragonRide.inbound
            )
        }
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dragonRide.inbound.airline,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End)
            ) {
                PriceButtonText(price = dragonRide.price)
            }
        }
    }
}

@Composable
fun PriceButtonText(price: Double) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 10.sp)) {
                append("€")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                append(price.toString())
            }
        }
    )
}

@Composable
fun FlightTimeInformationRow(title: String, flight: Flight) {
    Text(
        text = title,
        fontWeight = FontWeight.Light,
        color = Color.DarkGray,
        modifier = Modifier.padding(8.dp)
    )

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = flight.departureTime
        )
        Text(
            text = flight.arrivalTime
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterComponent(
    sliderRange: ClosedFloatingPointRange<Float>,
    onThumbValueChange: (Float, Float) -> Unit
) {

    Column {
        Text(
            text = stringResource(id = R.string.filter_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
    }

    var sliderPosition by remember { mutableStateOf(sliderRange) }

    RangeSlider(
        steps = 5,
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = sliderRange,
        onValueChangeFinished = {
            onThumbValueChange(sliderPosition.start, sliderPosition.endInclusive)
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )

    Text(
        text = stringResource(
            R.string.filter_range,
            sliderPosition.start.toInt(),
            sliderPosition.endInclusive.toInt()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}
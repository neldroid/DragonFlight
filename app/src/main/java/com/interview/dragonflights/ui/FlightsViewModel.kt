package com.interview.dragonflights.ui

import androidx.lifecycle.*
import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.domain.GetDragonRidesHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightsViewModel @Inject constructor(private val getDragonRidesHomeUseCase: GetDragonRidesHomeUseCase) :
    ViewModel() {

    private val _flights: MutableLiveData<Map<String, List<DragonRide>>> = MutableLiveData()
    private val _minPrice: MutableLiveData<Double> = MutableLiveData()
    private val _maxPrice: MutableLiveData<Double> = MutableLiveData()

    val flights: LiveData<Map<String, List<DragonRide>>> = _flights
    val minPrice: LiveData<Double> = _minPrice
    val maxPrice: LiveData<Double> = _maxPrice

    init {
        viewModelScope.launch {
            val fetchedFlights = getDragonRidesHomeUseCase.fetchFlights()
            _flights.value = fetchedFlights

            val prices = fetchedFlights.flatMap { it.value }.map { it.price }
            _minPrice.value = prices.min()
            _maxPrice.value = prices.max()
        }
    }

    fun filterFlightsByPrice(minValue: Double, maxValue: Double) {
        _flights.value = getDragonRidesHomeUseCase.fetchFlights(minValue, maxValue)
    }
}


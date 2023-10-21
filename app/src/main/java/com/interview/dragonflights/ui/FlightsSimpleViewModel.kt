package com.interview.dragonflights.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.dragonflights.data.model.DragonRide
import com.interview.dragonflights.data.repository.DragonRideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightsSimpleViewModel @Inject constructor(
    private val dragonRepository: DragonRideRepository
):ViewModel() {

    private val _flights: MutableLiveData<List<DragonRide>> = MutableLiveData()
    val flights: LiveData<List<DragonRide>> = _flights

    init {
        viewModelScope.launch {
            dragonRepository.fetchDragonRides().collect { rides ->
                // Estos "rides" son los que colectamos gracias al flow
                // Luego seteo al mutable data .value con los propios rides
                _flights.value = rides
            }
        }
    }
}
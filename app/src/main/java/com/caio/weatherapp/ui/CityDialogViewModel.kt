package com.caio.weatherapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.caio.weatherapp.api.WeatherService
import com.caio.weatherapp.model.City
import com.google.android.gms.maps.model.LatLng

class CityDialogViewModel(
    private val service: WeatherService
) : ViewModel() {

    var query by mutableStateOf("")
    var suggestions by mutableStateOf<List<City>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun onQueryChanged(value: String) {
        query = value

        if (value.length < 3) {
            service.cancelAll()
            suggestions = emptyList()
            isLoading = false
        } else {
            service.cancelAll()
            suggestions = emptyList()
            isLoading = true

            service.search(query) { res ->
                val cities = res?.mapNotNull {
                    val name = it.name
                    val lat = it.lat
                    val lon = it.lon

                    if (name != null && lat != null && lon != null) {
                        City(
                            name,
                            location = LatLng(lat, lon)
                        )
                    } else {
                        null
                    }
                }

                suggestions = cities ?: emptyList()
                isLoading = false
            }
        }
    }
}
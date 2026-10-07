package com.caio.weatherapp.model

import com.google.android.gms.maps.model.LatLng

data class City (
    val name : String,
    val weather: String? = null,
    val location: LatLng? = null
)

private fun getCities() = List(20) { i ->
    City(name = "Cidade $i", weather = "Carregando clima...")
}

fun getCities(dummy: Unit = Unit): List<City> = getCities()
package com.caio.weatherapp.model

import com.google.android.gms.maps.model.LatLng

data class City(
    val name: String,
    var location: LatLng? = null
)
/*
private fun getCities() = List(20) { i ->
    City(name = "Cidade $i", weather = "Carregando clima...")
}

fun getCities(dummy: Unit = Unit): List<City> = getCities()*/
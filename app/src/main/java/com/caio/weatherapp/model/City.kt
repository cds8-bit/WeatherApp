package com.caio.weatherapp.model

data class City (
    val name : String,
    val weather: String? = null,
    val location: String? = null
)

private fun getCities() = List(20) { i ->
    City(name = "Cidade $i", weather = "Carregando clima...")
}


package com.caio.weatherapp.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.caio.weatherapp.api.WeatherService
import com.caio.weatherapp.api.toWeather
import com.caio.weatherapp.db.fb.FBCity
import com.caio.weatherapp.db.fb.FBDatabase
import com.caio.weatherapp.db.fb.FBUser
import com.caio.weatherapp.db.fb.toFBCity
import com.caio.weatherapp.model.City
import com.caio.weatherapp.model.User
import com.caio.weatherapp.model.Weather
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModel(), FBDatabase.Listener {

    private val _user = mutableStateOf<User?>(null)

    val user: User?
        get() = _user.value

    private val _cities = mutableStateMapOf<String, City>()

    val cities: List<City> by derivedStateOf {
        _cities.values.toList().sortedBy { it.name }
    }

    private val _weather = mutableStateMapOf<String, Weather>()

    init {
        db.setListener(this)
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(
                    City(
                        name = name,
                        location = LatLng(lat, lng)
                    ).toFBCity()
                )
            }
        }
    }

    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(
                    City(
                        name = name,
                        location = location
                    ).toFBCity()
                )
            }
        }
    }

    fun add(name: String, location: LatLng? = null) {
        val city = City(
            name = name,
            location = location
        )

        db.add(city.toFBCity())
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
        _cities.remove(city.name)
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
    }

    override fun onCityAdded(city: FBCity) {
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityUpdated(city: FBCity) {
        val name = city.name!!
        _cities.remove(name)
        _cities[name] = city.toCity()
    }

    override fun onCityRemoved(city: FBCity) {
        city.name?.let {
            _cities.remove(it)
        }
    }

    fun weather(name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING
    }

    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                _weather[name] = it.toWeather()
            }
        }
    }
}
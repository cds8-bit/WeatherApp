package com.caio.weatherapp.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.caio.weatherapp.api.WeatherService
import com.caio.weatherapp.db.fb.FBCity
import com.caio.weatherapp.db.fb.FBDatabase
import com.caio.weatherapp.db.fb.FBUser
import com.caio.weatherapp.db.fb.toFBCity
import com.caio.weatherapp.model.City
import com.caio.weatherapp.model.User
import com.google.android.gms.maps.model.LatLng

class MainViewModel (private val db: FBDatabase,
                     private val service : WeatherService
): ViewModel(), FBDatabase.Listener{
    private val _user = mutableStateOf<User?>(null)

    val user: User?
        get() = _user.value

    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities.toList()

    init {
        db.setListener(this)
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)).toFBCity())
            }
        }
    }
    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location).toFBCity())
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
        _cities.remove(city)
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
    }

    override fun onCityAdded(city: FBCity) {
        val newCity = city.toCity()

        if (_cities.none { it.name == newCity.name }) {
            _cities.add(newCity)
        }
    }

    override fun onCityUpdated(city: FBCity) {
        val updatedCity = city.toCity()
        val index = _cities.indexOfFirst {
            it.name == updatedCity.name
        }

        if (index != -1) {
            _cities[index] = updatedCity
        }
    }

    override fun onCityRemoved(city: FBCity) {
        _cities.removeAll {
            it.name == city.toCity().name
        }
    }
}
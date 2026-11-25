package com.caio.weatherapp.ui

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.caio.weatherapp.R
import com.caio.weatherapp.model.Weather
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current

    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        onMapClick = {
            viewModel.addCity(it)
        },
        cameraPositionState = camPosState,
        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true
        )
    ) {
        viewModel.cities.forEach { city ->
            key(city.name) {
                val location = city.location

                if (location != null) {
                    val weather = viewModel.weather(city.name)

                    val image = weather.bitmap
                        ?: ContextCompat.getDrawable(
                            context,
                            R.drawable.loading
                        )!!.toBitmap()

                    val icon = BitmapDescriptorFactory.fromBitmap(
                        Bitmap.createScaledBitmap(
                            image,
                            120,
                            120,
                            false
                        )
                    )

                    val desc = if (weather == Weather.LOADING) {
                        "Carregando clima..."
                    } else {
                        weather.desc
                    }

                    val marker = remember {
                        MarkerState(position = location)
                    }

                    Marker(
                        state = marker,
                        icon = icon,
                        title = city.name,
                        snippet = desc
                    )
                }
            }
        }
    }
}
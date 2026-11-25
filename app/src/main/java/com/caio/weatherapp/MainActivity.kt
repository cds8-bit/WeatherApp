package com.caio.weatherapp

import BottomNavItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hasRoute
import com.caio.weatherapp.api.WeatherService
import com.caio.weatherapp.db.fb.FBDatabase
import com.caio.weatherapp.ui.CityDialog
import com.caio.weatherapp.ui.CityDialogViewModel
import com.caio.weatherapp.ui.CityDialogViewModelFactory
import com.caio.weatherapp.ui.MainViewModel
import com.caio.weatherapp.ui.MainViewModelFactory
import com.caio.weatherapp.ui.nav.BottomNavBar
import com.caio.weatherapp.ui.nav.MainNavHost
import com.caio.weatherapp.ui.theme.WeatherAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val fbDB = remember { FBDatabase() }
            val weatherService = remember { WeatherService() }

            val viewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(fbDB, weatherService)
            )

            val cityDialogService = remember { WeatherService() }

            val cityDialogViewModel: CityDialogViewModel = viewModel(
                factory = CityDialogViewModelFactory(cityDialogService)
            )

            val navController = rememberNavController()
            var showDialog by remember { mutableStateOf(false) }

            val currentRoute = navController.currentBackStackEntryAsState()
            val showButton = currentRoute.value?.destination?.hasRoute(Route.List::class) == true

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = {}
            )

            WeatherAppTheme {
                if (showDialog) {
                    CityDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { city ->
                            if (city.isNotBlank()) viewModel.addCity(city)
                            showDialog = false
                        },
                        viewModel = cityDialogViewModel
                    )
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Bem-vindo/a ${viewModel.user?.name ?: "Convidado"}!")
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        Firebase.auth.signOut()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        val items = listOf(
                            BottomNavItem.HomeButton,
                            BottomNavItem.ListButton,
                            BottomNavItem.MapButton,
                        )
                        BottomNavBar(viewModel, navController, items)
                    },
                    floatingActionButton = {
                        if (showButton) {
                            FloatingActionButton(
                                onClick = {
                                    cityDialogViewModel.query = ""
                                    cityDialogViewModel.onQueryChanged("")
                                    showDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        MainNavHost(navController = navController, viewModel = viewModel)
                    }
                    LaunchedEffect(viewModel.page) {
                        navController.navigate(viewModel.page) {
                            // Volta pilha de navegação até HomePage (startDest).
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}

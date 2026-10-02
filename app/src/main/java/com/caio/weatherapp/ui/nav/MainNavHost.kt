package com.caio.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.caio.weatherapp.model.MainViewModel
import com.caio.weatherapp.ui.theme.HomePage
import com.caio.weatherapp.ui.theme.ListPage
import com.caio.weatherapp.ui.theme.MapPage

@Composable
fun MainNavHost(navController: NavHostController,
                modifier: Modifier = Modifier, viewModel: MainViewModel
) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(modifier = modifier, viewModel = viewModel) }
        composable<Route.List> { ListPage(modifier = modifier, viewModel = viewModel) }
        composable<Route.Map> { MapPage(modifier = modifier, viewModel = viewModel) }
    }
}



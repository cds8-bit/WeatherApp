package com.caio.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.caio.weatherapp.ui.MainViewModel
import com.caio.weatherapp.ui.HomePage
import com.caio.weatherapp.ui.ListPage
import com.caio.weatherapp.ui.MapPage

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier
    ) {
        composable<Route.Home> {
            HomePage(
                modifier = Modifier,
                viewModel = viewModel
            )
        }

        composable<Route.List> {
            ListPage(
                modifier = Modifier,
                viewModel = viewModel
            )
        }

        composable<Route.Map> {
            MapPage(
                modifier = Modifier,
                viewModel = viewModel
            )
        }
    }
}

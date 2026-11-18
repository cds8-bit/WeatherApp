package com.caio.weatherapp.ui.nav

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.caio.weatherapp.ui.MainViewModel

@Composable
fun BottomNavBar(
    viewModel: MainViewModel,
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination

    NavigationBar(
        contentColor = Color.Black
    ) {
        items.forEach { item ->

            val selected = when (item.route) {
                Route.Home -> currentRoute?.route == "com.caio.weatherapp.ui.nav.Route.Home"
                Route.List -> currentRoute?.route == "com.caio.weatherapp.ui.nav.Route.List"
                Route.Map -> currentRoute?.route == "com.caio.weatherapp.ui.nav.Route.Map"
            }

            NavigationBarItem(
                selected = selected,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp
                    )
                },
                alwaysShowLabel = true,
                onClick = {
                    viewModel.page = item.route

                    when (item.route) {
                        Route.Home -> navController.navigate(Route.Home)
                        Route.List -> navController.navigate(Route.List)
                        Route.Map -> navController.navigate(Route.Map)
                    }
                }
            )
        }
    }
}

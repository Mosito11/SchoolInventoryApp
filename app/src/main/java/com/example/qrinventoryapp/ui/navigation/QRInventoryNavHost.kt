package com.example.qrinventoryapp.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.qrinventoryapp.ui.AppViewModelProvider
import com.example.qrinventoryapp.ui.home.AppMode
import com.example.qrinventoryapp.ui.home.HomeDestination
import com.example.qrinventoryapp.ui.home.HomeScreen
import com.example.qrinventoryapp.ui.scan.ScanScreen
import com.example.qrinventoryapp.ui.scan.ScanScreenDestination
import com.example.qrinventoryapp.ui.scan.ScanViewModel

@Composable
fun QRInventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToScan = { mode, userId, roomId -> navController.navigate(ScanScreenDestination.createRoute(mode, userId, roomId)) },
                navigateToQuit = { (navController.context as? ComponentActivity)?.finish() }
            )
        }
        composable(
            route = ScanScreenDestination.routeWithArgs,
            arguments = listOf(
            navArgument(ScanScreenDestination.modeArg) {
                type = NavType.StringType
                defaultValue = AppMode.CONTROL.name
            },
            navArgument(ScanScreenDestination.userIdArg) {
                type = NavType.IntType
                //nullable = true nemoze byt Int nullable
                defaultValue = -1
            },
            navArgument(ScanScreenDestination.roomIdArg) {
                type = NavType.IntType
                //nullable = true nemoze byt Int nullable
                defaultValue = -1
            }
            )
        ) { backStackEntry ->
                val viewModel: ScanViewModel = viewModel(factory = AppViewModelProvider.Factory)

                ScanScreen(
                    navigateBack = { navController.popBackStack()  },
                    saveIncorrectItem = { viewModel.saveIncorrectItem() },
                    newScan = { viewModel.clearScan() },
                    navBackStackEntry = backStackEntry
                )
            }
        }
}
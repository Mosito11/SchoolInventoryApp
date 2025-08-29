package com.example.schoolinventoryapp

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.schoolinventoryapp.ui.navigation.SchoolInventoryNavHost

@Composable
fun SchoolInventoryApp(navController: NavHostController = rememberNavController()) {
    SchoolInventoryNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolInventoryTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier
    )
}
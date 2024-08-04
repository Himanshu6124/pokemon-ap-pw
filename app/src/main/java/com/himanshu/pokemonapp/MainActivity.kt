package com.himanshu.pokemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.himanshu.pokemonapp.ui.pokemondetail.PokemonDetailScreen
import com.himanshu.pokemonapp.ui.pokemonlist.PokemonListScreen
import com.himanshu.pokemonapp.ui.theme.PokemonAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonAppTheme {
                Pokemon()
            }
        }
    }
}

@Composable
fun Pokemon() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            PokemonListScreen(navController = navController)
        }
        composable(
            "detail/{pokemonId}",
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId")
            pokemonId?.let {
                PokemonDetailScreen(pokemonId = it)
            }
        }
    }
}
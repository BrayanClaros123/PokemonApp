package com.example.pokeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokeapp.ui.PokemonDetailScreen
import com.example.pokeapp.ui.PokemonListScreen
import com.example.pokeapp.ui.splash.SplashScreen
import com.example.pokeapp.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val pokemonViewModel: PokemonViewModel = viewModel()

            NavHost(navController, startDestination = "splash") {
                composable("splash") { SplashScreen(navController, pokemonViewModel) }
                composable("pokemonList") { PokemonListScreen(navController, pokemonViewModel) }
                composable("pokemonDetail/{pokemonId}") { backStackEntry ->
                    PokemonDetailScreen(
                        pokemonId = backStackEntry.arguments?.getString("pokemonId"),
                        onBackPressed = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}
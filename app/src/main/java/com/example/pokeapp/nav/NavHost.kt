package com.example.pokeapp.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokeapp.modelviewintent.detail.PokemonDetailIntent
import com.example.pokeapp.modelviewintent.detail.PokemonDetailViewModel
import com.example.pokeapp.modelviewintent.list.PokemonListIntent
import com.example.pokeapp.modelviewintent.list.PokemonListViewModel
import com.example.pokeapp.ui.PokemonDetailScreen
import com.example.pokeapp.ui.PokemonListScreen
import com.example.pokeapp.ui.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val pokemonListViewModel: PokemonListViewModel = viewModel()
    val pokemonDetailViewModel: PokemonDetailViewModel = viewModel()
    val state by pokemonListViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("pokemonList") {
            PokemonListScreen(
                state = state,
                onLoadPokemons = {
                    pokemonListViewModel.handleIntent(PokemonListIntent.LoadPokemons)
                },
                onPokemonClick = { pokemonId ->
                    navController.navigate("pokemonDetail/$pokemonId")
                }
            )
        }

        composable("pokemonDetail/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""
            val state by pokemonDetailViewModel.state.collectAsState()

            PokemonDetailScreen(
                pokemonId = pokemonId,
                state = state,
                onLoadDetails = { id ->
                    pokemonDetailViewModel.handleIntent(PokemonDetailIntent.LoadPokemonDetails(id))
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}

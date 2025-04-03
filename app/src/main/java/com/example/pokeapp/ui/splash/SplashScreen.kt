package com.example.pokeapp.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pokeapp.R
import com.example.pokeapp.viewmodel.`is`.PokemonIntent
import com.example.pokeapp.viewmodel.`is`.PokemonState
import com.example.pokeapp.viewmodel.PokemonViewModel

@Composable
fun SplashScreen(navController: NavController, pokemonViewModel: PokemonViewModel = viewModel()) {
    val scale = remember { Animatable(0f) }
    val state by pokemonViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        pokemonViewModel.handleIntent(PokemonIntent.LoadPokemons)
    }

    LaunchedEffect(state) {
        if (state is PokemonState.Success) {
            navController.navigate("pokemonList") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    LaunchedEffect(key1 = scale) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { OvershootInterpolator(4f).getInterpolation(it) }
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.pokemon_logo),
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando Pokémon...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

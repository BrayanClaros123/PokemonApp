package com.example.pokeapp.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pokeapp.ui.pokemon.PokemonTypePill
import com.example.pokeapp.ui.pokemon.TypeFilterDropdown
import com.example.pokeapp.viewmodel.PokemonViewModel
import com.example.pokeapp.viewmodel.`is`.PokemonIntent
import com.example.pokeapp.viewmodel.`is`.PokemonState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(navController: NavController, pokemonViewModel: PokemonViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val state by pokemonViewModel.state.collectAsState()

    val transition = rememberInfiniteTransition()
    val shimmerColor by transition.animateColor(
        initialValue = Color(0xFFFFF176),
        targetValue = Color(0xFFFFD700),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val dorado = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFD700),
            Color(0xFFFFA500),
            Color(0xFFFFD700)
        )
    )
    val plateado = Brush.linearGradient(
        colors = listOf(
            Color(0xFFC0C0C0),
            Color(0xFF808080),
            Color(0xFFC0C0C0)
        )
    )

    LaunchedEffect(Unit) {
        pokemonViewModel.handleIntent(PokemonIntent.LoadPokemons)
    }

    when (state) {
        is PokemonState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is PokemonState.Error -> Text(text = "Error: ${(state as PokemonState.Error).message}")
        is PokemonState.Success -> {
            val pokemonList = (state as PokemonState.Success).pokemons
            val allTypes = pokemonList.flatMap { it.types ?: emptyList() }.map { it.type.name }.distinct()
            val filteredPokemonList = pokemonList.filter { pokemon ->
                (searchQuery.isEmpty() || pokemon.name.contains(searchQuery, ignoreCase = true)) &&
                        (selectedType == null || pokemon.types?.any { it.type.name == selectedType } == true)
            }

            Column(modifier = Modifier.fillMaxSize()
                .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E1E1E), Color(0xFF3A3A3A))
                )
            )) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .border(2.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar Pokémon", color = Color.Black) },
                        textStyle = TextStyle(color = Color.Black),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(plateado),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = selectedType?.capitalize() ?: "Filtrar por tipo",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TypeFilterDropdown(
                        allTypes = allTypes,
                        selectedType = selectedType,
                        onTypeSelected = { selectedType = it }
                    )
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredPokemonList) { pokemon ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 4.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.Gray,
                                            Color.LightGray,
                                            Color.DarkGray,
                                        )
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable { navController.navigate("pokemonDetail/${pokemon.id}") },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Gray,
                                                Color.LightGray
                                            )
                                        )
                                    )

                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "#${pokemon.id} - ${pokemon.name.capitalize()}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(dorado)
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row {
                                            pokemon.types?.forEachIndexed { index, typeSlot ->
                                                PokemonTypePill(type = typeSlot.type.name)
                                                if (index < pokemon.types.size - 1) {
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                }
                                            }
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(120.dp)
                                            .background(
                                                brush = Brush.radialGradient(
                                                    colors = listOf(shimmerColor, Color.Transparent),
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png"),
                                            contentDescription = pokemon.name,
                                            modifier = Modifier.size(100.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        if (state is PokemonState.Success && filteredPokemonList.isNotEmpty()) {
                            LaunchedEffect(Unit) {
                                pokemonViewModel.handleIntent(PokemonIntent.LoadPokemons)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
        else -> {}
    }
}
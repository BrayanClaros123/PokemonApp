package com.example.pokeapp.viewmodel.`is`

import com.example.pokeapp.model.Pokemon

sealed class PokemonState {
    object Loading : PokemonState()
    data class Success(val pokemons: List<Pokemon>) : PokemonState()
    data class Selected(val pokemon: Pokemon, val description: String) : PokemonState()
    data class Error(val message: String) : PokemonState()
}

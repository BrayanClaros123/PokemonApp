package com.example.pokeapp.modelviewintent.list

import com.example.pokeapp.model.domain.PokemonInfo

sealed class PokemonListState {
    object Loading : PokemonListState()
    data class Success(val pokemons: List<PokemonInfo>) : PokemonListState()
    data class Error(val message: String) : PokemonListState()
}
package com.example.pokeapp.modelviewintent.detail

import com.example.pokeapp.model.Pokemon


sealed class PokemonDetailState {
    object Loading : PokemonDetailState()
    data class Success(val pokemon: Pokemon, val description: String) : PokemonDetailState()
    data class Error(val message: String) : PokemonDetailState()
}
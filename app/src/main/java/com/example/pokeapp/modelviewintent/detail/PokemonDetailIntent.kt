package com.example.pokeapp.modelviewintent.detail

sealed class PokemonDetailIntent {
    data class LoadPokemonDetails(val id: String) : PokemonDetailIntent()
}
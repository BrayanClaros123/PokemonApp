package com.example.pokeapp.modelviewintent.list

sealed class PokemonListIntent {
    object LoadPokemons : PokemonListIntent()
}
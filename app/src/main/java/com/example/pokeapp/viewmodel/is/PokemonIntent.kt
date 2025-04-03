package com.example.pokeapp.viewmodel.`is`

sealed class PokemonIntent {
    object LoadPokemons : PokemonIntent()
    data class SelectPokemon(val id: String?) : PokemonIntent()
}
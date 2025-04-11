package com.example.pokeapp.modelviewintent.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.network.PokemonApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel : ViewModel() {
    private val _state = MutableStateFlow<PokemonDetailState>(PokemonDetailState.Loading)
    val state: StateFlow<PokemonDetailState> = _state

    fun handleIntent(intent: PokemonDetailIntent) {
        when (intent) {
            is PokemonDetailIntent.LoadPokemonDetails -> fetchPokemonDetails(intent.id)
        }
    }

    private fun fetchPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            try {
                val pokemon = PokemonApi.retrofitService.getPokemonID(pokemonId)
                val species = PokemonApi.retrofitService.getPokemonSpecies(pokemonId)
                val description = species.flavor_text_entries.firstOrNull { it.language.name == "en" }?.flavor_text
                    ?: "No description available."
                _state.value = PokemonDetailState.Success(pokemon, description)
            } catch (e: Exception) {
                _state.value = PokemonDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
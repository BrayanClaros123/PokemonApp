package com.example.pokeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.network.PokemonApi
import com.example.pokeapp.viewmodel.`is`.PokemonIntent
import com.example.pokeapp.viewmodel.`is`.PokemonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val _state = MutableStateFlow<PokemonState>(PokemonState.Loading)
    val state: StateFlow<PokemonState> = _state.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20
    private var isLoading = false

    init {
        handleIntent(PokemonIntent.LoadPokemons)
    }

    fun handleIntent(intent: PokemonIntent) {
        when (intent) {
            is PokemonIntent.LoadPokemons -> fetchPokemonList(reset = false)
            is PokemonIntent.SelectPokemon -> fetchPokemonDetails(intent.id.toString())
        }
    }


    private fun fetchPokemonList(reset: Boolean = false) {
        if (isLoading) return
        isLoading = true

        if (reset) {
            currentOffset = 0
        }

        viewModelScope.launch {
            try {
                val response = PokemonApi.retrofitService.getPokemonList(offset = currentOffset, limit = pageSize)
                val newPokemonList = response.results.map { PokemonApi.retrofitService.getPokemonDetails(it.name) }

                val existingList = if (reset) emptyList() else (_state.value as? PokemonState.Success)?.pokemons ?: emptyList()
                _state.value = PokemonState.Success(existingList + newPokemonList)

                if (newPokemonList.isNotEmpty()) {
                    currentOffset += pageSize
                }
            } catch (e: Exception) {
                _state.value = PokemonState.Error(e.message ?: "Unknown error")
                Log.e("PokemonViewModel", "Error fetching Pokemon list", e)
            }
            isLoading = false
        }
    }

    private fun fetchPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                if (currentState is PokemonState.Selected && currentState.pokemon.id.toString() == pokemonId) {
                    return@launch
                }

                _state.value = PokemonState.Loading

                val pokemon = PokemonApi.retrofitService.getPokemonID(pokemonId)
                val species = PokemonApi.retrofitService.getPokemonSpecies(pokemonId)
                val description = species.flavor_text_entries.firstOrNull { it.language.name == "en" }?.flavor_text
                    ?: "No description available."

                _state.value = PokemonState.Selected(pokemon, description)

            } catch (e: Exception) {
                _state.value = PokemonState.Error(e.message ?: "Unknown error")
                Log.e("PokemonViewModel", "Error fetching details", e)
            }
        }
    }

}
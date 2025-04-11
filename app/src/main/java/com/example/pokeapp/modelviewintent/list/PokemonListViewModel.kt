package com.example.pokeapp.modelviewintent.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.data.PokemonRepository
import com.example.pokeapp.model.domain.PokemonInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PokemonListState>(PokemonListState.Loading)
    val state: StateFlow<PokemonListState> = _state

    private var currentOffset = 0
    private val pageSize = 20
    private val loadedPokemonList = mutableListOf<PokemonInfo>()

    fun handleIntent(intent: PokemonListIntent) {
        when (intent) {
            is PokemonListIntent.LoadPokemons -> fetchPokemonList()
        }
    }

    private fun fetchPokemonList() {
        viewModelScope.launch {
            _state.value = PokemonListState.Loading
            try {
                val newPokemonList = repository.getPokemonList(offset = currentOffset, limit = pageSize)
                loadedPokemonList.addAll(newPokemonList)
                _state.value = PokemonListState.Success(loadedPokemonList.toList())
                currentOffset += pageSize
            } catch (e: Exception) {
                _state.value = PokemonListState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

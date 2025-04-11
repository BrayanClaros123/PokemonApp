package com.example.pokeapp.data

import android.util.Log
import com.example.pokeapp.model.domain.PokemonInfo
import com.example.pokeapp.network.PokemonApi
import com.example.pokeapp.data.toDomain
import com.example.pokeapp.network.PokemonApiService
import javax.inject.Inject


class PokemonRepository @Inject constructor(
    private val api: PokemonApiService
) {
    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): List<PokemonInfo> {
        val response = api.getPokemonList(offset, limit)
        return response.results.map {
            val details = api.getPokemonDetails(it.name)
            Log.d("PokemonRepository", "Obtenido detalles de ${it.name}")
            val species = api.getPokemonSpecies(details.id.toString())
            details.toDomain()
        }
    }

    suspend fun getPokemonByName(name: String): PokemonInfo {
        val details = api.getPokemonDetails(name)
        val species = api.getPokemonSpecies(details.id.toString())
        return details.toDomain()
    }
}


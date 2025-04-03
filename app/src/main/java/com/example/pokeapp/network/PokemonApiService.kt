package com.example.pokeapp.network

import com.example.pokeapp.model.Pokemon
import com.example.pokeapp.model.PokemonResponse
import com.example.pokeapp.model.PokemonSpecies
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://pokeapi.co/api/v2/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface PokemonApiService {
    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): Pokemon

    @GET("pokemon/{id}")
    suspend fun getPokemonID(@Path("id") id: String): Pokemon

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: String): PokemonSpecies

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonResponse


}

object PokemonApi {
    val retrofitService: PokemonApiService by lazy {
        retrofit.create(PokemonApiService::class.java)
    }
}
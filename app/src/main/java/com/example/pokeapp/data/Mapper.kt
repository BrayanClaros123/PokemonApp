package com.example.pokeapp.data

import com.example.pokeapp.model.Pokemon
import com.example.pokeapp.model.domain.PokemonInfo
import com.example.pokeapp.model.PokemonSpecies

fun Pokemon.toDomain(): PokemonInfo {
    return PokemonInfo(
        id = id,
        name = name,
        types = types,
        height = height,
        weight = weight,
        abilities = abilities,
        stats = stats
    )
}


fun PokemonSpecies.extractDescription(): String {
    return flavor_text_entries
        .firstOrNull { it.language.name == "en" }
        ?.flavor_text
        ?.replace("\n", " ")
        ?.replace("\u000c", " ")
        ?: "No description available."
}

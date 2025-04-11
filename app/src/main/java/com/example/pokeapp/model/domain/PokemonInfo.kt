package com.example.pokeapp.model.domain

import com.example.pokeapp.model.AbilitySlot
import com.example.pokeapp.model.StatSlot
import com.example.pokeapp.model.TypeSlot

data class PokemonInfo(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>?,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>,
    val stats: List<StatSlot>
)

data class TypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String,
    val url: String
)

data class AbilitySlot(
    val ability: Ability
)

data class Ability(
    val name: String
)

data class StatSlot(
    val base_stat: Int,
    val stat: Stat
)

data class Stat(
    val name: String
)

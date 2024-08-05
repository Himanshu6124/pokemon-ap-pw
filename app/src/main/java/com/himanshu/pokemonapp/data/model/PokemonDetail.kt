package com.himanshu.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val sprites: Sprites,
    val abilities: List<SingleAbility>,
    val stats: List<Stat>
)

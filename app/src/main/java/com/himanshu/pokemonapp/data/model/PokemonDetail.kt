package com.himanshu.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val sprites: Sprites,
    val abilities: List<SingleAbility>,
)

data class Ability(val name :String, val url : String)

data class SingleAbility(
    val ability :Ability,
    @SerializedName("is_hidden")
    val isHidden : Boolean ,
    val slot : Int)

data class Sprites(
    @SerializedName("back_default")
    val backDefault: String,
    @SerializedName("back_shiny")
    val backShiny: String,
    @SerializedName("front_default")
    val frontDefault: String,
    @SerializedName("front_shiny")
    val frontShiny: String
)
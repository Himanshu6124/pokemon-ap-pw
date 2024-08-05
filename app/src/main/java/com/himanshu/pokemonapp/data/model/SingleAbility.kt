package com.himanshu.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class SingleAbility(
    val ability: Ability,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    val slot: Int
)
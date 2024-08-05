package com.himanshu.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val stat: StatDetail
)

data class StatDetail(
    val name: String
)
package com.himanshu.pokemonapp.util

object Helper {
    fun Int.toPokemonImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$this.png"
    }
}
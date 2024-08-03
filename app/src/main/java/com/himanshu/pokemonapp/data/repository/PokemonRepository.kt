package com.himanshu.pokemonapp.data.repository

import com.himanshu.pokemonapp.data.model.Pokemon
import com.himanshu.pokemonapp.data.model.PokemonDetail
import com.himanshu.pokemonapp.data.network.PokemonApiService
import javax.inject.Inject

class PokemonRepository @Inject constructor(private val apiService: PokemonApiService) {
    suspend fun getPokemonList(): List<Pokemon> = apiService.getPokemonList().results

    suspend fun getPokemonDetail(id: Int): PokemonDetail = apiService.getPokemonDetail(id)
}

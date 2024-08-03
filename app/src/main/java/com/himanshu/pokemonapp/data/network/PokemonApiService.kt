package com.himanshu.pokemonapp.data.network

import com.himanshu.pokemonapp.data.model.PokemonDetail
import com.himanshu.pokemonapp.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): PokemonDetail
}

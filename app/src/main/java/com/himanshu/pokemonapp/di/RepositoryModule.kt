package com.himanshu.pokemonapp.di

import com.himanshu.pokemonapp.data.network.PokemonApiService
import com.himanshu.pokemonapp.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /* Provides an instance of PokemonRepository */
    @Provides
    @Singleton
    fun provideRepository(apiService: PokemonApiService): PokemonRepository {
        return PokemonRepository(apiService)
    }
}
package com.himanshu.pokemonapp.util

object Constants {
    const val BASE_URL = "https://pokeapi.co/api/v2/" // Base url for end point
    const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB cache size
    const val MAX_AGE_SECONDS = 300 // 5 minutes max age for cache
    const val MAX_STALE_SECONDS = 60 * 60 * 24 * 7 // 1 week max stale for cache
}
package com.himanshu.pokemonapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.himanshu.pokemonapp.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://pokeapi.co/api/v2/"
    private const val CACHE_SIZE = (10 * 1024 * 1024).toLong() // 10 MB
    private const val MAX_AGE = 300 // 5 minutes
    private const val MAX_STALE = 60 * 60 * 24 * 7 // 1 week

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context, cache: Cache): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient(context, cache))
        .build()

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir, "http-cache"), CACHE_SIZE)
    }

    private fun provideOkHttpClient(context: Context, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context))
                    request.newBuilder().header("Cache-Control", "public, max-age=$MAX_AGE").build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=$MAX_STALE").build()
                chain.proceed(request)
            }
            .build()
    }

    private fun hasNetwork(@ApplicationContext context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Provides
    @Singleton
    fun providePokemonApiService(retrofit: Retrofit): PokemonApiService = retrofit.create(PokemonApiService::class.java)

}

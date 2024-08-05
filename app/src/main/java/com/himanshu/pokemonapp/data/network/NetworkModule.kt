package com.himanshu.pokemonapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.himanshu.pokemonapp.util.Constants.BASE_URL
import com.himanshu.pokemonapp.util.Constants.CACHE_SIZE_BYTES
import com.himanshu.pokemonapp.util.Constants.MAX_AGE_SECONDS
import com.himanshu.pokemonapp.util.Constants.MAX_STALE_SECONDS

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    /* Provides Retrofit instance configured with base URL, Gson converter, and OkHttpClient */
    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context, cache: Cache): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(context, cache))
            .build()
    }

    /* Provides Cache instance with specified size, located in the app's cache directory */
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDir = File(context.cacheDir, "http-cache")
        return Cache(cacheDir, CACHE_SIZE_BYTES)
    }

    /* Provides OkHttpClient instance with cache interceptor based on network availability */
    private fun provideOkHttpClient(context: Context, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().apply {
                    if (hasNetwork(context)) {
                        // Use max-age if network is available
                        header("Cache-Control", "public, max-age=$MAX_AGE_SECONDS")
                    } else {
                        // Use max-stale if network is unavailable
                        header("Cache-Control", "public, only-if-cached, max-stale=$MAX_STALE_SECONDS")
                    }
                }.build()
                chain.proceed(request)
            }
            .build()
    }

    /* Checks if the device has an active internet connection */
    private fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /* Provides PokemonApiService instance created by Retrofit */
    @Provides
    @Singleton
    fun providePokemonApiService(retrofit: Retrofit): PokemonApiService {
        return retrofit.create(PokemonApiService::class.java)
    }
}

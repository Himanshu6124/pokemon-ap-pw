package com.himanshu.pokemonapp.ui.pokemondetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.pokemonapp.data.model.PokemonDetail
import com.himanshu.pokemonapp.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing and providing detailed information about a specific Pokémon.
 */
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val logTag = "PokemonDetailViewModel"

    /* LiveData to hold the details of a Pokémon */
    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> get() = _pokemonDetail

    /* LiveData to indicate loading state */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /* LiveData to hold any error messages */
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * Loads detailed information about a Pokémon based on its ID.
     * Updates LiveData and handles any errors that occur during the network request.
     */
    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true) // Set loading state to true
                val detail = repository.getPokemonDetail(id) // Fetch Pokémon details
                Log.i(logTag, detail.toString()) // Log the details for debugging
                _pokemonDetail.postValue(detail) // Update LiveData with the fetched details
            } catch (exception: Exception) {
                Log.e(logTag, "Exception occurred while fetching Pokémon detail", exception)
                _errorMessage.postValue(exception.message) // Update LiveData with error message
            } finally {
                _isLoading.postValue(false) // Reset loading state
            }
        }
    }
}

package com.himanshu.pokemonapp.ui.pokemonlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.pokemonapp.data.model.Pokemon
import com.himanshu.pokemonapp.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/* ViewModel responsible for managing and providing data for the Pokemon list */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val logTag = "PokemonListViewModel"

    /* LiveData to hold the list of Pokémon */
    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList

    /* LiveData to indicate loading state */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /* LiveData to hold any error messages */
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /* Flag to indicate if more data is available */
    private val _hasMoreData = MutableLiveData(true)

    /* Current page for pagination */
    private var currentPage = 0

    init {
        /* Load the initial list of Pokémon */
        loadPokemon()
    }

    /**
     * Loads more Pokémon and updates the LiveData.
     * Handles pagination and error cases.
     */
    fun loadPokemon() {
        /* Return if a load operation is already in progress */
        if (_isLoading.value == true || _hasMoreData.value == false) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                /* Calculate the offset based on the current page */
                val offset = currentPage * PAGE_SIZE
                val response = repository.getPokemonList(offset, PAGE_SIZE)
                Log.d(logTag, response.toString())

                // Check if there is more data to load
                _hasMoreData.postValue(response.isNotEmpty())

                // Update the Pokémon list with the new data
                val currentList = _pokemonList.value ?: emptyList()
                _pokemonList.postValue(currentList + response)

                // Increment the page count for the next load
                currentPage++
            } catch (exception: Exception) {
                Log.e(logTag, "Error occurred while fetching Pokémon", exception)
                _errorMessage.postValue(exception.message)
            } finally {
                // Reset loading state
                _isLoading.postValue(false)
            }
        }
    }

    companion object {
        // Constant for the number of items per page
        const val PAGE_SIZE = 20
    }
}

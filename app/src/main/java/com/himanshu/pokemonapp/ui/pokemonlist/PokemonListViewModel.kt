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

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val tag = "PokemonListViewModel"
    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    private var currentPage = 0

    init {
        loadMorePokemon()
    }

    fun loadMorePokemon() {
        if (isLoading.value == true) return
        _isLoading.value = true
        viewModelScope.launch {

            try {
                val offset = currentPage * PAGE_SIZE
                val response = repository.getPokemonList(offset, PAGE_SIZE)
                Log.d(tag, response.toString())
                val currentList = pokemonList.value ?: emptyList()
                _pokemonList.postValue(currentList + response)
                currentPage++
            }
            catch (ex : Exception){
                Log.e(tag, "exception occurred in get characters", ex)
                _errorMessage.postValue(ex.message)
            }
            finally {
                _isLoading.postValue(false)
            }

        }
    }
    companion object{
        const val PAGE_SIZE = 20

    }
}

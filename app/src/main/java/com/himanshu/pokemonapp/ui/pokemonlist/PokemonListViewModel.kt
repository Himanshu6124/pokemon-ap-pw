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

    init {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val res = repository.getPokemonList()
                Log.d(tag, res.toString())
                _pokemonList.value = res

            } catch (ex: Exception) {
                Log.e(tag, "exception occurred in get characters", ex)
                _errorMessage.value = ex.message
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

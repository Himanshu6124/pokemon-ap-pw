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
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {
    private val TAG = "PokemonListViewModel"
    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList

    init {
        viewModelScope.launch {
            val res  = repository.getPokemonList()
            Log.d(TAG,res.toString())
            _pokemonList.value = res
        }
    }
}

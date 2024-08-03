package com.himanshu.pokemonapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.pokemonapp.data.model.PokemonDetail
import com.himanshu.pokemonapp.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> get() = _pokemonDetail

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _pokemonDetail.value = repository.getPokemonDetail(id)
        }
    }
}

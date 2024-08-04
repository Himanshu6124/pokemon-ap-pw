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

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val tag = "PokemonDetailViewModel"
    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> get() = _pokemonDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val res = repository.getPokemonDetail(id)
                Log.i(tag, res.toString())
                _pokemonDetail.value = res
            } catch (ex: Exception) {
                Log.e(tag, "exception occurred in get characters", ex)
                _errorMessage.value = ex.message
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

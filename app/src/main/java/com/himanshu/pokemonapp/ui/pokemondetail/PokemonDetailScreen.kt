package com.himanshu.pokemonapp.ui.pokemondetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PokemonDetailScreen(pokemonId: Int, viewModel: PokemonDetailViewModel = hiltViewModel()) {
    val pokemonDetail by viewModel.pokemonDetail.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(true)


    LaunchedEffect(Unit) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    pokemonDetail?.let { detail ->

        Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Name: ${detail.name}", style = MaterialTheme.typography.displayMedium)
                Text(text = "Height: ${detail.height}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Weight: ${detail.weight}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    }
}
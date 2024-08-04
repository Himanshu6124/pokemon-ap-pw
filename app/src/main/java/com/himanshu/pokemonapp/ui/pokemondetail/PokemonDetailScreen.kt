package com.himanshu.pokemonapp.ui.pokemondetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PokemonDetailScreen(pokemonId: Int, viewModel: PokemonDetailViewModel = hiltViewModel()) {
    val pokemonDetail by viewModel.pokemonDetail.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    pokemonDetail?.let { detail ->
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
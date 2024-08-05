package com.himanshu.pokemonapp.ui.pokemondetail

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PokemonDetailScreen(pokemonId: Int, viewModel: PokemonDetailViewModel = hiltViewModel()) {
    val pokemonDetail by viewModel.pokemonDetail.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(true)
    val errorMessage by viewModel.errorMessage.observeAsState(null)
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    LaunchedEffect(key1 = errorMessage) {
        errorMessage?.let {
            Log.i("PokemonDetailViewModel", "error in launch effect")
            snackBarHostState.showSnackbar(
                message = errorMessage!!,
                duration = SnackbarDuration.Short,
            )
        }
    }

    Scaffold(
        topBar = { TopBar(pokemonName = pokemonDetail?.name ?: "Pokemon") },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            } else {

                pokemonDetail?.let { detail ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Text(
                            text = "Height: ${detail.height}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Weight: ${detail.weight}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(pokemonName: String) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = pokemonName.replaceFirstChar { it.uppercaseChar() },
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
    })
}
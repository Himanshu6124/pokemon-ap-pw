package com.himanshu.pokemonapp.ui.pokemonlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.himanshu.pokemonapp.R
import com.himanshu.pokemonapp.data.model.Pokemon
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by viewModel.pokemonList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(true)
    val errorMessage by viewModel.errorMessage.observeAsState(null)
    val listState = rememberLazyGridState()

    // Handle error messages with a Snackbar
    errorMessage?.let {
        Snackbar(
            modifier = Modifier.padding(8.dp)
        ) { Text(it) }
    }

    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && pokemonList.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(pokemonList) { pokemon ->
                        val id = pokemon.url.split("/").last { it.isNotEmpty() }.toInt()
                        val imageUrl =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
                        PokemonListItem(pokemon, imageUrl) {
                            navController.navigate("detail/$id")
                        }
                    }
                }

                // Scroll listener to load more data when the user scrolls to the bottom
                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                        .distinctUntilChanged()
                        .collect { index ->
                            if (index == pokemonList.size - 1 && !isLoading) {
                                viewModel.loadMorePokemon()
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(pokemon: Pokemon, imageUrl: String, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20)

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                imageModel = imageUrl,
                contentDescription = stringResource(R.string.pokemon_image),
                modifier = Modifier
                    .size(64.dp)
                    .padding(5.dp),
                contentScale = ContentScale.Crop,
                placeHolder = ImageBitmap.imageResource(R.drawable.loading_image),
                error = ImageVector.vectorResource(R.drawable.error_image)
            )
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercaseChar() },
                modifier = Modifier.padding(8.dp),
                color = Color.DarkGray,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            text = "Pokemon List",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
    })
}

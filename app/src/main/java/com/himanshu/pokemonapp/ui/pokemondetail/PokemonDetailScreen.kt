package com.himanshu.pokemonapp.ui.pokemondetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.himanshu.pokemonapp.R
import com.himanshu.pokemonapp.data.model.PokemonDetail
import com.himanshu.pokemonapp.data.model.SingleAbility
import com.himanshu.pokemonapp.data.model.Stat
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    // Observe Pokémon detail, loading state, and error message
    val pokemonDetail by viewModel.pokemonDetail.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(true)
    val errorMessage by viewModel.errorMessage.observeAsState(null)
    val snackBarHostState = remember { SnackbarHostState() }

    /* Load Pokémon details when screen is first displayed
    * Only load the Pokémon detail if it hasn't been loaded yet */
    val hasLoadedDetail = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pokemonId) {
        if (!hasLoadedDetail.value) {
            viewModel.loadPokemonDetail(pokemonId)
            hasLoadedDetail.value = true
        }
    }

    // Show error message in a SnackBar if there's any
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackBarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Main layout for the screen
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                // Display a progress indicator while loading
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            } else {
                // Display Pokémon details when available
                LazyColumn {
                    item {
                        pokemonDetail?.let { detail ->
                            PokemonDetailCard(pokemonDetail = detail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonDetailCard(pokemonDetail: PokemonDetail) {
    val imageScale = remember { Animatable(0f) }
    val textOpacity = remember { Animatable(0f) }

    // Animate image scale and text opacity
    LaunchedEffect(Unit) {
        imageScale.animateTo(1f, animationSpec = tween(durationMillis = 800))
        textOpacity.animateTo(1f, animationSpec = tween(durationMillis = 800))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Pokémon image
            GlideImage(
                imageModel = pokemonDetail.sprites.frontDefault,
                contentDescription = stringResource(R.string.pokemon_image),
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
                    .graphicsLayer(scaleX = imageScale.value, scaleY = imageScale.value),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pokémon name
            Text(
                text = pokemonDetail.name.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .graphicsLayer(alpha = textOpacity.value)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Pokémon weight and height
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Weight: ${pokemonDetail.weight}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Height: ${pokemonDetail.height}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            PokemonAbilities(abilities = pokemonDetail.abilities)
            Spacer(modifier = Modifier.height(16.dp))
            PokemonStats(stats = pokemonDetail.stats)
        }
    }
}

@Composable
fun PokemonAbilities(abilities: List<SingleAbility> = emptyList()) {
    val enterTransition =
        fadeIn(tween(durationMillis = 500)) + slideInVertically(tween(durationMillis = 500))
    val exitTransition =
        fadeOut(tween(durationMillis = 500)) + slideOutVertically(tween(durationMillis = 500))

    AnimatedVisibility(
        visible = abilities.isNotEmpty(),
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = "Abilities:",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            abilities.forEach { ability ->
                Text(
                    text = ability.ability.name.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PokemonStats(stats: List<Stat> = emptyList()) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Stats:",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        stats.forEach { stat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stat.stat.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.width(100.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { stat.baseStat / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stat.baseStat.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    modifier = Modifier.width(30.dp),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate total progress
        val totalBaseStat = stats.sumOf { it.baseStat }
        val maxBaseStat = stats.size * 100
        val totalProgress = totalBaseStat / maxBaseStat.toFloat()

        Text(
            text = "Total Progress",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { totalProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$totalBaseStat / $maxBaseStat",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

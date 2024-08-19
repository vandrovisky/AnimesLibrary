@file:OptIn(ExperimentalMaterial3Api::class)

package com.lorena.projetodevapps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.lorena.projetodevapps.ui.theme.ProjetodevappsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetodevappsTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("releases") { ReleasesScreen(navController) }
                    composable("animes") { AnimesScreen(navController) }
                    composable("mangas") { MangasScreen(navController) }
                }
            }
        }
    }
}


@Composable
fun HomeScreen(navController: NavHostController) {
    val komikax = FontFamily(Font(R.font.komikax)) // Referência à fonte Komika Axis

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0BBE4), Color(0xFF957DAD)) // Degradê de lilás ao roxo mais claro
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter // Alinha o conteúdo ao fundo da imagem
        ) {
            Image(
                painter = painterResource(id = R.drawable.principal),
                contentDescription = "Main Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Bem-vindo ao Anime Library",
                fontSize = 20.sp,
                color = Color.White, // Ajusta a cor do texto para branco
                fontFamily = komikax,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter) // Garantir que o texto esteja no pé da imagem
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botões de navegação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { navController.navigate("releases") }) {
                Text(text = "Lançamentos", fontFamily = komikax)
            }
            Button(onClick = { navController.navigate("animes") }) {
                Text(text = "Animes", fontFamily = komikax)
            }
            Button(onClick = { navController.navigate("mangas") }) {
                Text(text = "Mangás", fontFamily = komikax)
            }
        }

        // Grade de imagens
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Permitir rolagem vertical
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento entre linhas
        ) {
            val imageResources = listOf(
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,
                R.drawable.image5,
                R.drawable.image6,
                R.drawable.image7,
                R.drawable.image8,
                R.drawable.image9,
                R.drawable.image10,
                R.drawable.image11,
                R.drawable.image12
            )

            // Adicionando imagens em 3 linhas e 4 colunas
            for (row in 0 until 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Espaçamento igual entre as colunas
                ) {
                    for (col in 0 until 3) { // 3 colunas
                        val index = row * 3 + col
                        if (index < imageResources.size) {
                            Image(
                                painter = painterResource(id = imageResources[index]),
                                contentDescription = "Image ${index + 1}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 120.dp, height = 160.dp) // Tamanho maior
                                    .clip(RoundedCornerShape(8.dp)) // Borda arredondada suave
                                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)) // Borda branca
                            )
                        }
                    }
                }
            }
        }

        // Botão para subir ao topo
        FloatingActionButton(
            onClick = {
                // Implementação simples para rolar até o topo
                // Essa parte requer suporte para coroutines e seria implementada adequadamente
            },
            containerColor = Color.White,
            contentColor = Color.Black,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End) // Alinhar à direita
        ) {
            Text("⬆") // Seta para cima
        }
    }
}

@Composable
fun ReleasesScreen(navController: NavHostController) {
    val releases = remember { mutableStateOf<List<Anime>>(emptyList()) }
    val selectedAnime = remember { mutableStateOf<Anime?>(null) }

    // Fetch releases when screen is displayed
    LaunchedEffect(Unit) {
        try {
            releases.value = ApiClient.api.getTopSpecialAnimes()
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error, e.g., show a Toast or Snackbar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lançamentos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE0BBE4))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            releases.value.forEach { anime ->
                Text(
                    text = anime.title,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedAnime.value = anime
                        }
                )
            }
        }
    }

    // Show anime detail dialog if an anime is selected
    selectedAnime.value?.let {
        AnimeDetailDialog(anime = it) {
        selectedAnime.value = null
    }
    }
}

@Composable
fun AnimesScreen(navController: NavHostController) {
    val query = remember { mutableStateOf("") }
    val searchResults = remember { mutableStateOf<List<Media>>(emptyList()) }
    val selectedMedia = remember { mutableStateOf<Media?>(null) }

    // Perform search when query changes
    LaunchedEffect(query.value) {
        if (query.value.isNotEmpty()) {
            try {
                val results = ApiClient.api.searchAnime(query.value)
                searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF957DAD))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Search Bar
            TextField(
                value = query.value,
                onValueChange = { query.value = it },
                label = { Text("Search Anime") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // Trigger LaunchedEffect by updating the query
                    if (query.value.isNotEmpty()) {
                        query.value = query.value.trim() // Ensure it doesn't run unnecessarily
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Search Results
            if (searchResults.value.isEmpty()) {
                Text("No results found", color = Color.White)
            } else {
                LazyColumn {
                    items(searchResults.value) { media ->
                        Text(
                            text = media.title,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    selectedMedia.value = media
                                }
                        )
                    }
                }
            }
        }
    }

    // Show media detail dialog if media is selected
    selectedMedia.value?.let {
        MediaDetailDialog(media = it) {
            selectedMedia.value = null
        }
    }
}

@Composable
fun MangasScreen(navController: NavHostController) {
    val query = remember { mutableStateOf("") }
    val searchResults = remember { mutableStateOf<List<Media>>(emptyList()) }
    val selectedMedia = remember { mutableStateOf<Media?>(null) }

    // Perform search when query changes
    LaunchedEffect(query.value) {
        if (query.value.isNotEmpty()) {
            try {
                val results = ApiClient.api.searchManga(query.value)
                searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error, e.g., show a Toast or Snackbar
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mangás") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFB39DDB))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Search Bar
            TextField(
                value = query.value,
                onValueChange = { query.value = it },
                label = { Text("Search Manga") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // Trigger LaunchedEffect by updating the query
                    if (query.value.isNotEmpty()) {
                        query.value = query.value.trim() // Ensure it doesn't run unnecessarily
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Search Results
            if (searchResults.value.isEmpty()) {
                Text("No results found", color = Color.White)
            } else {
                LazyColumn {
                    items(searchResults.value) { media ->
                        Text(
                            text = media.title,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    selectedMedia.value = media
                                }
                        )
                    }
                }
            }
        }
    }

    // Show media detail dialog if media is selected
    selectedMedia.value?.let {
        MediaDetailDialog(media = it) {
            selectedMedia.value = null
        }
    }
}


@Composable
fun AnimeDetailDialog(anime: Anime?, onDismissRequest: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    if (anime != null) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = anime.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        painter = rememberAsyncImagePainter(anime.picture_url),
                        contentDescription = anime.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Text(text = "Score: ${anime.score}")
                    Text(text = "Type: ${anime.type}")
                    Text(text = "Episodes: ${anime.nbEps}")
                    Text(text = "Aired: ${anime.startDate} - ${anime.endDate}")
                    Text(text = "Members: ${anime.members}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            uriHandler.openUri(anime.myanimelist_url)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("View on MyAnimeList")
                    }
                }
            }
        }
    }
}

@Composable
fun MediaDetailDialog(media: Media?, onDismissRequest: () -> Unit) {
    if (media != null) {
        val uriHandler = LocalUriHandler.current
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = media.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        painter = rememberAsyncImagePainter(media.thumbnail),
                        contentDescription = media.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Text(text = "Score: ${media.score}")
                    Text(text = "Type: ${media.type}")
                    Text(text = "Episodes/Chapters: ${media.nbEps}")
                    Text(text = "Published: ${media.startDate} - ${media.endDate}")
                    Text(text = "Members: ${media.members}")
                    Text(text = "Rating: ${media.rating}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            uriHandler.openUri(media.url.toString())
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("View on MyAnimeList")
                    }
                }
            }
        }
    }
}

package com.invenkode.vybe

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen(navController: NavController) {
    // ➊ Launcher for the system file picker (audio/video)
    val filePickerLauncher = rememberLauncherForActivityResult(OpenDocument()) { uri: Uri? ->
        uri?.let {
            // TODO: replace this with your own handling (e.g. add to playlist)
            Log.d("HomeScreen", "User picked file: $it")
        }
    }

    // Mock “recently listened” list
    val recentList = listOf(
        VideoItem("X1", "https://i.ytimg.com/vi/X1/hqdefault.jpg", "Funky Beat", "2:34"),
        VideoItem("Y2", "https://i.ytimg.com/vi/Y2/hqdefault.jpg", "Chill Vibes", "4:15"),
        VideoItem("Z3", "https://i.ytimg.com/vi/Z3/hqdefault.jpg", "Up-Tempo Mix", "3:20")
    )

    Scaffold(
        bottomBar = {
            VybeBottomBar(
                onSearch = { navController.navigate("search") },
                onAddFile = {
                    // mime types: audio/*, video/* (you can adjust)
                    filePickerLauncher.launch(arrayOf("audio/*", "video/*"))
                },
                onPlaylist = { navController.navigate("playlist") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // single “royal purple” gradient
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF512DA8), // Deep Purple 700
                            Color(0xFF7E57C2)  // Deep Purple 400
                        )
                    )
                )
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Continue Listening",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))

                LazyRow {
                    items(recentList) { video ->
                        RecentVideoCard(video) {
                            navController.navigate("video/${video.videoId}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VybeBottomBar(
    onSearch: () -> Unit,
    onAddFile: () -> Unit,
    onPlaylist: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = onSearch,
            icon =      { Icon(Icons.Filled.Search, contentDescription = "Search") },
            label =     { Text("Search") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onAddFile,
            icon =      { Icon(Icons.Filled.FileUpload, contentDescription = "Add File") },
            label =     { Text("Add File") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onPlaylist,
            icon =      { Icon(Icons.Filled.PlaylistPlay, contentDescription = "Playlist") },
            label =     { Text("Playlist") }
        )
    }
}
@Composable
private fun RecentVideoCard(video: VideoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(end = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(video.thumbnailUrl),
                contentDescription = video.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
            Spacer(Modifier.height(4.dp))
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Text(
                    text = video.duration,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
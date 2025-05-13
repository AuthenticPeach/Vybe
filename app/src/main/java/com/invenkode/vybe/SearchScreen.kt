package com.invenkode.vybe

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<VideoItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search YouTube") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            isLoading = true
            // Launch a coroutine to perform the search (update BingSearchService accordingly)
            CoroutineScope(Dispatchers.IO).launch {
                // Assume BingSearchService.searchYouTube(query) returns List<String> of raw URLs.
                val rawResults = BingSearchService.searchYouTube(query)
                // Map each URL to a VideoItem using our helper.
                results = rawResults.map { url ->
                    val id = extractVideoId(url)
                    VideoItem(
                        videoId = id,
                        thumbnailUrl = "https://img.youtube.com/vi/$id/0.jpg",
                        title = "Title for $id", // Replace with real title extraction if available
                        duration = "3:45"        // Replace with real duration if available
                    )
                }
                isLoading = false
            }
        }) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(results) { video ->
                    VideoListItem(video = video) {
                        // Navigate to a video player screen using the video ID
                        navController.navigate("video/${video.videoId}")
                    }
                }
            }
        }
    }
}

@Composable
fun VideoListItem(video: VideoItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(video.thumbnailUrl),
            contentDescription = video.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = video.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = video.duration, style = MaterialTheme.typography.bodySmall)
        }
    }
}

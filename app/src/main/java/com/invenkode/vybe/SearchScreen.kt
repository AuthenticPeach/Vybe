// SearchScreen.kt
package com.invenkode.vybe

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi                   // ← add this
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class                        // ← opt into keyboard API
)
@Composable
fun SearchScreen(
    navController: NavController,
    vm: SearchViewModel = viewModel()
) {
    val query by vm.query.collectAsState()
    val suggestions by vm.suggestions.collectAsState()
    val results by vm.results.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    // to keep focus on the TextField and keep the keyboard up
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded && suggestions.isNotEmpty(),
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    vm.onQueryChange(it)
                    expanded = true
                },
                label = { Text("Search YouTube") },
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = (6 * 56).dp)
            ) {
                suggestions.take(6).forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            vm.onQueryChange(suggestion)
                            expanded = false
                            focusRequester.requestFocus()
                            keyboardController?.show()
                            vm.performSearch()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { vm.performSearch() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            if (results.isEmpty() && suggestions.isNotEmpty()) {
                Text(
                    text = "Did you mean “${suggestions.first()}”?",
                    modifier = Modifier
                        .clickable {
                            vm.onQueryChange(suggestions.first())
                            vm.performSearch()
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LazyColumn {
                items(results) { video ->
                    VideoListItem(video) {
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
            Text(video.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(video.duration, style = MaterialTheme.typography.bodySmall)
        }
    }
}
package com.invenkode.vybe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("search") }) {
            Text("Search YouTube")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: Implement File Picker */ }) {
            Text("Add Local File")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("playlist") }) {
            Text("View Playlist")
        }
    }
}

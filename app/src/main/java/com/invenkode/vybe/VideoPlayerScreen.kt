package com.invenkode.vybe

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxWidth
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoPlayerScreen(videoId: String) {
    val context = LocalContext.current

    // Create and remember the YouTubePlayerView
    val youTubePlayerView = remember {
        YouTubePlayerView(context).apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }
    }

    // Dispose of the player when the composable leaves the composition to avoid leaks.
    DisposableEffect(Unit) {
        onDispose {
            youTubePlayerView.release()
        }
    }

    AndroidView(
        factory = { youTubePlayerView },
        modifier = Modifier.fillMaxWidth()
    )
}

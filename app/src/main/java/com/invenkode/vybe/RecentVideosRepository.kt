// File: RecentVideosRepository.kt
package com.invenkode.vybe

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object RecentVideosRepository {
    private const val MAX_RECENTS = 10

    // backing state
    private val _recentVideos = MutableStateFlow<List<VideoItem>>(emptyList())
    val recentVideos: StateFlow<List<VideoItem>> = _recentVideos

    /** Call this whenever the user plays a video. */
    fun addVideo(video: VideoItem) {
        // drop any existing entry with same ID, insert at front, cap size
        val updated = _recentVideos.value
            .filterNot { it.videoId == video.videoId }
            .toMutableList()
            .apply {
                add(0, video)
                if (size > MAX_RECENTS) removeLast()
            }
        _recentVideos.value = updated
    }
}

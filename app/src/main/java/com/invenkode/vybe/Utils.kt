package com.invenkode.vybe

import android.net.Uri

// Extracts the "v" parameter from a YouTube URL (e.g. https://www.youtube.com/watch?v=abc123)
fun extractVideoId(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("v") ?: ""
}

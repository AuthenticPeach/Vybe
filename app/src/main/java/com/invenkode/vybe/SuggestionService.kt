// SuggestionService.kt
package com.invenkode.vybe

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

object SuggestionService {
    private val client = OkHttpClient()

    /**
     * Hits Bing's JSON autosuggest endpoint and returns a list of suggestion strings.
     * Example URL: https://api.bing.com/osjson.aspx?query=beetles+sing
     */
    fun fetchSuggestions(query: String): List<String> {
        if (query.length < 2) return emptyList()
        val encoded = query.trim().replace(" ", "+")
        val url = "https://api.bing.com/osjson.aspx?query=$encoded"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0")
            .build()

        return try {
            client.newCall(request).execute().use { resp ->
                val body = resp.body?.string() ?: return emptyList()
                // response is like: [ "orig", ["sugg1","sugg2",…] ]
                val arr = JSONArray(body)
                val suggestions = arr.optJSONArray(1) ?: return emptyList()
                List(suggestions.length()) { i -> suggestions.optString(i) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

package com.invenkode.vybe

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

object BingSearchService {
    private val client = OkHttpClient()

    fun searchYouTube(query: String): List<String> {
        val encodedQuery = "site:youtube.com $query".replace(" ", "+")
        val searchUrl = "https://www.bing.com/search?q=$encodedQuery"

        val request = Request.Builder()
            .url(searchUrl)
            .header("User-Agent", "Mozilla/5.0")
            .build()

        return try {
            val response = client.newCall(request).execute()
            val html = response.body?.string() ?: return emptyList()
            extractYouTubeLinks(html)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun extractYouTubeLinks(html: String): List<String> {
        val document = Jsoup.parse(html)
        val links = mutableListOf<String>()

        document.select("a[href]").forEach { element: org.jsoup.nodes.Element ->
            val link = element.attr("href")
            if (link.contains("youtube.com/watch") && !links.contains(link)) {
                links.add(link)
            }
        }
        return links
    }
}

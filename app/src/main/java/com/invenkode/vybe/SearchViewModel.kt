package com.invenkode.vybe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    // 1) AUTOSUGGESTIONS on IO
    val suggestions: StateFlow<List<String>> = _query
        .debounce(300)
        .distinctUntilChanged()
        .mapLatest { q ->
            if (q.isBlank()) emptyList()
            else withContext(Dispatchers.IO) {
                SuggestionService.fetchSuggestions(q)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 2) MAIN SEARCH RESULTS
    private val _results = MutableStateFlow<List<VideoItem>>(emptyList())
    val results = _results.asStateFlow()

    // 3) LOADING FLAG
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun onQueryChange(new: String) {
        _query.value = new
    }

    fun performSearch() {
        val q = _query.value.ifBlank { return }
        viewModelScope.launch {
            _isLoading.value = true

            // Switch to IO for the network call:
            val raw = withContext(Dispatchers.IO) {
                BingSearchService.searchYouTube(q)
            }

            // Back on Main, update UI state
            _results.value = raw.map { url ->
                val id = extractVideoId(url)
                VideoItem(
                    videoId = id,
                    thumbnailUrl = "https://img.youtube.com/vi/$id/0.jpg",
                    title = "Title for $id",
                    duration = "3:45"
                )
            }
            _isLoading.value = false
        }
    }
}

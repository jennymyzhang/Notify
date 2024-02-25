package com.example.notify.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SearchModel(): ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _notes = MutableStateFlow(dummyNotes)
    @OptIn(FlowPreview::class)
    val notes = searchText
        .debounce(500L)
        .onEach {_isSearching.update{true}}
        .combine(_notes) { text, notes ->
            if (text.isBlank()) {
                notes
            } else {
                delay(1000)
                notes.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach {_isSearching.update{false}}
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _notes.value
        )
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

}


data class Note(
    val term: String,
    val courseCode: String,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$term$courseCode",
            "$term $courseCode",
            "${term.first()} ${courseCode.first()}"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase=true)
        }
    }
}

private val dummyNotes = listOf(
    Note(
        term = "2024WINTER",
        courseCode = "STAT330"
    ),
    Note(
        term = "2024WINTER",
        courseCode = "STAT333"
    ),
    Note(
        term = "2024WINTER",
        courseCode = "CS240"
    ),
    Note(
        term = "2024WINTER",
        courseCode = "MATH235"
    ),
    Note(
        term = "2024WINTER",
        courseCode = "CS251"
    ),
    Note(
        term = "2024WINTER",
        courseCode = "CS346"
    ),
)
package com.example.news.presentation.new_library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.New
import com.example.news.domain.use_case.DeleteFromDatabase
import com.example.news.domain.use_case.GetFromDatabase
import com.example.news.domain.use_case.SaveToDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getFromDatabase: GetFromDatabase,
    private val saveToDatabase: SaveToDatabase,
    private val deleteFromDatabase: DeleteFromDatabase
): ViewModel() {

    private val _uiState = MutableStateFlow<List<New>>(emptyList())
    val uiState: StateFlow<List<New>> get() = _uiState.asStateFlow()

    init {
        updateUiState()
    }

    fun updateUiState() {
        viewModelScope.launch {
            getFromDatabase.invoke().collectLatest { list ->
                _uiState.update { list }
            }
        }
    }

    fun deleteFromBookmark(new: New) {
        handleDelete(new.copy(isBookmark = false))
        updateBookmarkShelf(new, false)
    }

    fun deleteFromFav(new: New) {
        handleDelete(new.copy(isFav = false))
        updateFavShelf(new, false)
    }

    private fun handleDelete(new: New) {
        viewModelScope.launch {
            with(new) {
                when {
                    isBookmark || isFav -> saveToDatabase(new)
                    else -> deleteFromDatabase(new)
                }
            }
        }
    }

    private fun updateBookmarkShelf(new: New, isBookmark: Boolean) {
        _uiState.update { list ->
            list.map {
                if (it == new) it.copy(isBookmark = isBookmark) else it
            }
        }
    }

    private fun updateFavShelf(new: New, isFav: Boolean) {
        _uiState.update { list ->
            list.map {
                if (it == new) it.copy(isFav = isFav) else it
            }
        }
    }
}
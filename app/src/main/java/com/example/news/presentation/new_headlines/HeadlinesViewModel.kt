package com.example.news.presentation.new_headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.New
import com.example.news.domain.use_case.DeleteFromDatabase
import com.example.news.domain.use_case.GetFromDatabase
import com.example.news.domain.use_case.GetHeadlines
import com.example.news.domain.use_case.SaveToDatabase
import com.example.news.presentation.model.CategoryType
import com.example.news.presentation.utils.GenericUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val getHeadlines: GetHeadlines,
    private val getFromDatabase: GetFromDatabase,
    private val saveToDatabase: SaveToDatabase,
    private val deleteFromDatabase: DeleteFromDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenericUiState<New>())
    val uiState: StateFlow<GenericUiState<New>> get() = _uiState.asStateFlow()

    private val _category = MutableStateFlow(CategoryType.GENERAL)
    val category: StateFlow<CategoryType> get() = _category.asStateFlow()

    init {
        updateUiState(getHeadlines(CategoryType.US.category))
    }

    private fun updateUiState(remoteFlow: Flow<Result<List<New>>>) {
        viewModelScope.launch {
            _uiState.update { GenericUiState(isLoading = true) }
            remoteFlow.combine(getFromDatabase.invoke()) { remote, local ->
                Pair(remote, local)
            }.collectLatest { pair ->
                println("headline pair triggered")
                pair.first.onSuccess { data ->
                    val databaseMap = pair.second.associateBy { it.url }
                    val updatedData = data.map {
                        databaseMap[it.url] ?: it
                    }
                    _uiState.update {
                        GenericUiState(
                            uiList = updatedData,
                            isLoading = false
                        )
                    }
                }.onFailure { throwable ->
                    _uiState.update {
                        GenericUiState(
                            isLoading = false,
                            error = throwable.message ?: "Unexpected error occurred!",
                        )
                    }
                }
            }
        }
    }

    fun updateUiByCategory(type: CategoryType) {
        if (type != category.value) {
            _category.update { type }
            updateUiState(
                getHeadlines(type.category)
            )
        }
    }

    fun bookmarkNew(new: New) {
        handleBookmark(new.copy(isBookmark = true))
    }

    fun unBookmarkNew(new: New) {
        handleBookmark(new.copy(isBookmark = false))
    }

    private fun handleBookmark(new: New) {
        viewModelScope.launch {
            with(new) {
                when {
                    isBookmark -> saveToDatabase(new)
                    isFav -> saveToDatabase(new)
                    else -> deleteFromDatabase(new)
                }
            }
        }
    }
}


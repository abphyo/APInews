package com.example.news.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.Publisher
import com.example.news.domain.use_case.GetPublishers
import com.example.news.presentation.model.CategoryType
import com.example.news.presentation.model.CountryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getPublishers: GetPublishers
): ViewModel() {

    private val _sourceUiState = MutableStateFlow(GenericUiState<Publisher>())
    val sourceUiState: StateFlow<GenericUiState<Publisher>> get() = _sourceUiState.asStateFlow()

    private val _category = MutableStateFlow(CategoryType.GENERAL)
    val category: StateFlow<CategoryType> get() = _category.asStateFlow()

    private val _country = MutableStateFlow(CountryType.WORLD)
    val country: StateFlow<CountryType> get() = _country.asStateFlow()

    private fun <T> updateUiState(
        resultFlow: Flow<Result<List<T>>>,
        stateFlow: MutableStateFlow<GenericUiState<T>>
    ) {
        viewModelScope.launch {
            stateFlow.update { GenericUiState(isLoading = true) }
            resultFlow.collectLatest { result ->
                result.onSuccess { data ->
                    stateFlow.update {
                        GenericUiState(
                            uiList = data,
                            isLoading = false
                        )
                    }
                }.onFailure { throwable ->
                    stateFlow.update {
                        GenericUiState(
                            error = throwable.message ?: "Unexpected error occurred!",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun updatePublishersUi(type: CategoryType) {
        updateUiState(
            getPublishers(
                category = type.category,
                country = country.value.country,
                language = ""
            ),
            _sourceUiState
        )
    }

    fun updatePublishersUiAgain(type: CountryType) {
        if (type != country.value) {
            _country.update { type }
            updateUiState(
                getPublishers(
                    category = category.value.category,
                    country = type.country,
                    language = ""
                ),
                _sourceUiState
            )
        }
    }
}
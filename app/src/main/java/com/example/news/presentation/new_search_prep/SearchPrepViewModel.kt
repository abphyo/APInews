package com.example.news.presentation.new_search_prep

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.New
import com.example.news.domain.use_case.DeleteFromDatabase
import com.example.news.domain.use_case.GetByEverythingSearch
import com.example.news.domain.use_case.GetByHeadlinesSearch
import com.example.news.domain.use_case.GetFromDatabase
import com.example.news.domain.use_case.SaveToDatabase
import com.example.news.presentation.model.HistoryState
import com.example.news.presentation.model.SearchInType
import com.example.news.presentation.utils.ConnectivityObserver
import com.example.news.presentation.utils.DataStoreManager
import com.example.news.presentation.utils.GenericUiState
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
class SearchPrepViewModel @Inject constructor(
    application: Application,
    private val networkConnectivityObserver: ConnectivityObserver,
    private val getByHeadlinesSearch: GetByHeadlinesSearch,
    private val getByEverythingSearch: GetByEverythingSearch,
    private val getFromDatabase: GetFromDatabase,
    private val saveToDatabase: SaveToDatabase,
    private val deleteFromDatabase: DeleteFromDatabase
): ViewModel() {
    private val dataStore = DataStoreManager(application)

    private val _searchHistory = MutableStateFlow(HistoryState())
    val searchHistory: StateFlow<HistoryState> get() = _searchHistory.asStateFlow()

    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.UNAVAILABLE)
    val networkStatus: StateFlow<ConnectivityObserver.Status> get() = _networkStatus.asStateFlow()

    private val _searchIn = MutableStateFlow(listOf<String>())
    val searchIn: StateFlow<List<String>> get() = _searchIn.asStateFlow()

    private val _uiState = MutableStateFlow(GenericUiState<New>())
    val uiState: StateFlow<GenericUiState<New>> get() = _uiState.asStateFlow()

    init {
        getSearchHistory()
        observeNetwork()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collectLatest { status ->
                println("network: $status")
                _networkStatus.update { status }
            }
        }
    }

    fun updateResultHeadlines(query: String) {
        updateUiState(getByHeadlinesSearch(keyword = query))
    }

    fun updateResultEverything(query: String) {
        searchIn.value.let { list ->
            val searchIn = if (list.isNotEmpty()) {
                list.joinToString(separator = ",") { it }
            } else ""
            println("searchIn: $searchIn")
            updateUiState(
                getByEverythingSearch(
                    keyword = query,
                    searchIn = searchIn
                )
            )
        }
    }

    private fun updateUiState(remoteFlow: Flow<Result<List<New>>>) {
        viewModelScope.launch {
            _uiState.update { GenericUiState(isLoading = true) }
            remoteFlow.collectLatest { result ->
                result.onSuccess { remoteData ->
                    println("remoteData: $remoteData")
                    getFromDatabase().collectLatest { localData ->
                        println("database flow triggered")
                        val databaseMap = localData.associateBy { it.url }
                        val updatedData = remoteData.map {
                            databaseMap[it.url] ?: it
                        }
                        println("updated data: $updatedData")
                        _uiState.update {
                            GenericUiState(
                                uiList = updatedData,
                                isLoading = false
                            )
                        }
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

    fun addToSearchIn(type: SearchInType) {
        _searchIn.update { it.plus(type.searchIn) }
    }

    fun removeFromSearchIn(type: SearchInType) {
        _searchIn.update { it.minus(type.searchIn) }
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
                    isBookmark or isFav -> saveToDatabase(new)
                    else -> deleteFromDatabase(new)
                }
            }
        }
    }

    private fun getSearchHistory() {
        viewModelScope.launch {
            dataStore.getStringList().collectLatest { list ->
                println("get: $list")
                _searchHistory.update { HistoryState( primaryKey = it.primaryKey + 1 , historyList = list ) }
            }
        }
    }

    private fun updateSearchHistory(list: MutableList<String>) {
        viewModelScope.launch {
            println("set: $list")
            dataStore.setStringList(list)
        }
    }

    fun pushToQueue(query: String) {
        viewModelScope.launch {
            with(searchHistory.value.historyList) {
                if (contains(query)) remove(query).also { add(query) } else add(query)
                if (size > 5) removeFirst()
                updateSearchHistory(this)
            }
        }
    }

    fun popFromQueue(item: String) {
        viewModelScope.launch {
            with(searchHistory.value.historyList) {
                if (!isNullOrEmpty()) remove(item)
                updateSearchHistory(this)
            }
        }
    }

}
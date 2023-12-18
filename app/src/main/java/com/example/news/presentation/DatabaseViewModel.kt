package com.example.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.use_case.DeleteFromDatabase
import com.example.news.domain.use_case.GetFromDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val getFromDatabase: GetFromDatabase,
    private val deleteFromDatabase: DeleteFromDatabase
): ViewModel() {

    init {
        renewDatabase()
    }

    fun renewDatabase() {
        viewModelScope.launch {
            getFromDatabase.invoke().collectLatest { list ->
                println("from database: $list")
                list.map {
                    if (!it.isBookmark && !it.isFav) {
                        deleteFromDatabase.invoke(it)
                    }
                }
            }
        }
    }
}
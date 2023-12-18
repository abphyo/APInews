package com.example.news.presentation.new_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.New
import com.example.news.domain.use_case.DeleteFromDatabase
import com.example.news.domain.use_case.SaveToDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val saveToDatabase: SaveToDatabase,
    private val deleteFromDatabase: DeleteFromDatabase
) : ViewModel() {

    fun handleBookmark(new: New, action: Boolean) {
        viewModelScope.launch {
            if (action) saveToDatabase(new.copy(isBookmark = true))
            else if (!new.isFav) deleteFromDatabase(new)
        }
    }

    fun handleFav(new: New, action: Boolean) {
        viewModelScope.launch {
            if (action) saveToDatabase(new.copy(isFav = true))
            else if (!new.isBookmark) deleteFromDatabase(new)
        }
    }
}
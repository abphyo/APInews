package com.example.news.domain.use_case

import com.example.news.data.repository.DatabaseRepoImpl
import com.example.news.domain.model.SearchFilter
import javax.inject.Inject

class SaveFilterInstance @Inject constructor(private val repo: DatabaseRepoImpl) {
    suspend operator fun invoke(searchFilter: SearchFilter) = repo.saveFilterInstance(searchFilter)
}
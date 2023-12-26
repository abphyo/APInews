package com.example.news.domain.use_case

import com.example.news.data.repository.DatabaseRepoImpl
import com.example.news.domain.model.New
import javax.inject.Inject

class SaveToDatabase @Inject constructor(private val repo: DatabaseRepoImpl) {
    suspend operator fun invoke(new: New) = repo.saveNew(new)
}
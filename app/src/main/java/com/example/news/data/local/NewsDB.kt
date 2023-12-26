package com.example.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.data.local.dao.NewDao
import com.example.news.data.local.entities.NewEntity
import com.example.news.data.local.entities.SearchFilterEntity

@Database(entities = [NewEntity::class, SearchFilterEntity::class], version = 2)
abstract class NewsDB : RoomDatabase() {
    abstract fun newDao(): NewDao
}
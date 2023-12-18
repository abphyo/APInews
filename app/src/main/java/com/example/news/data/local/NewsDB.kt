package com.example.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.data.local.dao.NewDao
import com.example.news.data.local.entities.NewEntity

@Database(entities = [NewEntity::class], version = 1)
abstract class NewsDB : RoomDatabase() {

    abstract fun newDao(): NewDao

}
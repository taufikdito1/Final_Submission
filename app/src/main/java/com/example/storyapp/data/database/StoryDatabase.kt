package com.example.storyapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyapp.data.model.story.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}
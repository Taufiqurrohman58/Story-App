package com.barengsaya.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barengsaya.storyapp.data.api.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<ListStoryItem>)

    @Query("SELECT * FROM story_table")
    fun getStories(): PagingSource<Int, ListStoryItem>
    @Query("DELETE FROM story_table")
    suspend fun deleteAllStories()
}

package com.barengsaya.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.data.api.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(
                token = "Bearer $token",
                page = position,
                size = params.loadSize
            )

            LoadResult.Page(
                data = response.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}


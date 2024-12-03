package com.barengsaya.storyapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.barengsaya.storyapp.DataDummy
import com.barengsaya.storyapp.MainDispatcherRule
import com.barengsaya.storyapp.data.Repository
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.getOrAwaitValue
import com.barengsaya.storyapp.view.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: Repository

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())

        Mockito.`when`(repository.getStoriesPaging(Mockito.anyString()))
            .thenReturn(MutableLiveData(data))

        val mainViewModel = MainViewModel(repository)
        val actualStories: PagingData<ListStoryItem> =
            mainViewModel.getStoriesPaging("fake_token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        Assert.assertEquals(0, differ.snapshot().size)
    }
    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyStories = DataDummy.generateDummyStories()
        val data: PagingData<ListStoryItem> = PagingData.from(dummyStories)

        Mockito.`when`(repository.getStoriesPaging(Mockito.anyString()))
            .thenReturn(MutableLiveData(data))

        val mainViewModel = MainViewModel(repository)
        val actualStories: PagingData<ListStoryItem> =
            mainViewModel.getStoriesPaging("fake_token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0], differ.snapshot()[0])
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

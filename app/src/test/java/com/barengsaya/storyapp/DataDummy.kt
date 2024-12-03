package com.barengsaya.storyapp

import com.barengsaya.storyapp.data.api.response.ListStoryItem



object DataDummy {

    fun generateDummyStories(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = mutableListOf()
        for (i in 1..10) {
            val story = ListStoryItem(
                id = "id_$i",
                name = "Taufiq $i",
                description = "This is story $i",
                photoUrl = "https://image$i",
                createdAt = "EEEE, MMMM d, yyyy",
                lat = -6.2,
                lon = 106.8
            )
            items.add(story)
        }
        return items
    }
}

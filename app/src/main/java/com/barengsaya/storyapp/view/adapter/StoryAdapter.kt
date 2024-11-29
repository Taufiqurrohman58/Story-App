package com.barengsaya.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barengsaya.storyapp.data.api.response.ListStoryItem
import com.barengsaya.storyapp.databinding.ItemStoryBinding
import com.bumptech.glide.Glide

import android.app.Activity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import android.content.Intent
import com.barengsaya.storyapp.view.detail.DetailActivity

class StoryAdapter(
    private val stories: MutableList<ListStoryItem> = mutableListOf()
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.storyUsername.text = story.name
            binding.storyDescription.text = story.description

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.storyImage)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.STORY_ID, story.id)
                }

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair(binding.storyImage, "gambar"),
                )

                context.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = stories.size

    fun updateStories(newStories: List<ListStoryItem>) {
        stories.clear()
        stories.addAll(newStories)
    }
}

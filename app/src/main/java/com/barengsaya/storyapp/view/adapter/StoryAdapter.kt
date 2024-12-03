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
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.barengsaya.storyapp.R
import com.barengsaya.storyapp.view.detail.DetailActivity

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.storyUsername.text = story.name ?: "No Name"
            binding.storyDescription.text = story.description ?: "No Description"

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_place_holder)
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
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

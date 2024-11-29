package com.barengsaya.storyapp.view.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.barengsaya.storyapp.data.api.response.Story
import com.barengsaya.storyapp.databinding.ActivityDetailBinding
import com.barengsaya.storyapp.view.ViewModelFactory
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(STORY_ID) ?: return

        setupObserver()
        setupBackButton()
        viewModel.getDetailStory(storyId)
    }

    private fun setupObserver() {
        viewModel.story.observe(this) { story ->
            story?.let { populateDetail(it) }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun populateDetail(story: Story) {
        binding.apply {
            storyUsername.text = story.name
            storyDescription.text = story.description
            storyDate.text = formatDate(story.createdAt)

            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(storyImage)
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)

            val outputFormat = java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", java.util.Locale.US)
            outputFormat.format(date ?: "")
        } catch (e: Exception) {
            dateString
        }
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}

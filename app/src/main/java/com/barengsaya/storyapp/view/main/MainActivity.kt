package com.barengsaya.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.barengsaya.storyapp.R
import com.barengsaya.storyapp.databinding.ActivityMainBinding
import com.barengsaya.storyapp.view.ViewModelFactory
import com.barengsaya.storyapp.view.adapter.StoryAdapter
import com.barengsaya.storyapp.view.maps.MapsActivity
import com.barengsaya.storyapp.view.upload.UploadActivity
import com.barengsaya.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupRecyclerView()
        setupObservers()
        setupAction()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mapsButton -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.lougoutButton -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StoryAdapter()

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.stories.observe(this) { stories ->
            adapter.updateStories(stories)
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {


        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

}

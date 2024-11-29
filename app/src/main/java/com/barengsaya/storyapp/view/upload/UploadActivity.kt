package com.barengsaya.storyapp.view.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.barengsaya.storyapp.databinding.ActivityUploadBinding
import com.barengsaya.storyapp.view.ViewModelFactory
import com.barengsaya.storyapp.view.main.MainActivity

@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageUri = savedInstanceState?.getParcelable(CURRENT_IMAGE_URI_KEY)
        currentImageUri?.let { showImage() }

        binding.galeryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        val description = binding.descriptionEditText.text.toString()
        if (currentImageUri == null || description.isEmpty()) {
            Toast.makeText(this, "Tolong Masukkan Gambar dan Deskripsi", Toast.LENGTH_SHORT).show()
            return
        }

        val file = uriToFile(currentImageUri!!, this)
        val viewModel = ViewModelFactory.getInstance(this).create(UploadViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE

        viewModel.uploadStory(file, description,
            onSuccess = {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Upload berhasil!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            },
            onError = { error ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Upload gagal: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CURRENT_IMAGE_URI_KEY, currentImageUri)
    }

    companion object {
        private const val CURRENT_IMAGE_URI_KEY = "current_image_uri"
    }
}

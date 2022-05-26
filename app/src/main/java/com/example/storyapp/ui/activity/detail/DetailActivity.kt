package com.example.storyapp.ui.activity.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Detail Story"
        setDetailStory()
    }

    private fun setDetailStory() {
        val username = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val image = intent.getStringExtra(EXTRA_IMAGE)

        binding.apply {
            textName.text = username
            textDescription.text = description
            Glide.with(this@DetailActivity)
                .load(image)
                .into(imageStory)
        }
    }

    companion object{
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}
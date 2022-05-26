package com.example.storyapp.ui.activity.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.LoadingStateAdapter
import com.example.storyapp.data.adapter.ListAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.helper.DataStoreViewModel
import com.example.storyapp.ui.activity.login.LoginActivity
import com.example.storyapp.ui.activity.maps.MapsActivity
import com.example.storyapp.ui.activity.story.AddStoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainVIewModel>()
    private lateinit var binding: ActivityMainBinding
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"

        adapter = ListAdapter()

        validate()
        setRecyclerView()
        action()
    }

    private fun action() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity)
                    .toBundle()
            )
        }

        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity)
                    .toBundle()
            )
        }
    }

    private fun validate() {
        dataStoreViewModel.getSession().observe(this) { userSession ->
            if (!userSession.isLogin) {
                Log.d("tag", userSession.token)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity)
                        .toBundle()
                )
                finish()
            }
        }
    }

    private fun setRecyclerView() {
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            viewModel.story.observe(this@MainActivity) {
                adapter.submitData(lifecycle, it)
            }
        }
        viewModel.isLoading.observe(this) { showLoading(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logout_user_menu -> {
                dataStoreViewModel.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this@MainActivity, "Logout Success", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
            R.id.refresh_menu -> {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this@MainActivity, "Refresh page", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
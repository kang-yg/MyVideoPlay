package com.kyg.myvideoplayer.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.kyg.myvideoplayer.R
import com.kyg.myvideoplayer.databinding.ActivityMainBinding
import com.kyg.myvideoplayer.model.Status
import com.kyg.myvideoplayer.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private val videoListAdapter: VideoListAdapter by lazy {
        VideoListAdapter(callback = { url, title ->
            supportFragmentManager.fragments.find { it is PlayFragment }?.let {
                Log.d("AAA", "Selected item $url, $title")
                (it as PlayFragment).play(url, title)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        observeVideoItems()
        setPlayFragment()
    }

    private fun setPlayFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayFragment())
            .commit()
    }

    private fun setRecyclerView() {
        activityMainBinding.rvVideos.adapter = videoListAdapter
    }

    private fun observeVideoItems() {
        val progress: ProgressBar = activityMainBinding.progress
        mainActivityViewModel.res.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progress.visibility = View.GONE
                    it.data.let { res ->
                        res?.data.let { videoList ->
                            videoListAdapter.submitList(videoList)
                        }
                    }
                }

                Status.LOADING -> {
                    progress.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    progress.visibility = View.GONE
                    Snackbar.make(
                        activityMainBinding.root,
                        "Something went wrong",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
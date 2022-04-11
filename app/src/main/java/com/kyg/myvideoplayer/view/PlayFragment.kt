package com.kyg.myvideoplayer.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.material.snackbar.Snackbar
import com.kyg.myvideoplayer.R
import com.kyg.myvideoplayer.databinding.FragmentPlayBinding
import com.kyg.myvideoplayer.model.Status
import com.kyg.myvideoplayer.viewmodel.PlayFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayFragment : Fragment() {
    private lateinit var fragmentPlayBinding: FragmentPlayBinding
    private val playFragmentViewModel: PlayFragmentViewModel by viewModels()
    private val videoListAdapter: VideoListAdapter by lazy {
        VideoListAdapter(callback = { url, title ->
            this.play(url, title)
        })
    }
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false)
        return fragmentPlayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayoutEvent()
        setRecyclerView()
        observeVideoItems()
        initPlayer()
        setControlButton()
    }

    private fun setRecyclerView() {
        fragmentPlayBinding.fragmentRecyclerView.adapter = videoListAdapter
    }

    private fun observeVideoItems() {
        playFragmentViewModel.res.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data.let { res ->
                        res?.data.let { videoList ->
                            videoListAdapter.submitList(videoList)
                        }
                    }
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Snackbar.make(
                        fragmentPlayBinding.root,
                        "Something went wrong",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun initMotionLayoutEvent() {
        fragmentPlayBinding.playerMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                fragmentPlayBinding.let {
                    (activity as MainActivity).also {
                        it.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = progress
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
    }

    private fun initPlayer() {
        context?.let {
            player = ExoPlayer.Builder(it).build()
        }
        fragmentPlayBinding.let {
            it.playerView.player = player
            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        it.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_pause_24)
                    } else {
                        it.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                }
            })
        }
    }

    private fun setControlButton() {
        fragmentPlayBinding.bottomPlayerControlButton.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    fun play(url: String, title: String) {
        context?.let {
            val dataSourceFactory = DefaultDataSource.Factory(it)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.play()
        }

        fragmentPlayBinding.let {
            fragmentPlayBinding.playerMotionLayout.transitionToEnd()
            fragmentPlayBinding.bottomTitleTextView.text = title
        }
    }

    override fun onStop() {
        super.onStop()

        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.release()
    }
}
package com.kyg.myvideoplayer.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kyg.myvideoplayer.databinding.ItemVideoBinding
import com.kyg.myvideoplayer.model.VideoItem

class VideoListAdapter(val callback: (String, String) -> Unit) :
    ListAdapter<VideoItem, VideoListAdapter.VideoItemViewHolder>(diffUtil) {
    inner class VideoItemViewHolder(private val itemVideoBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(itemVideoBinding.root) {
        fun bind(itemVideo: VideoItem) {
            itemVideoBinding.titleTextView.text = itemVideo.title
            itemVideoBinding.subTitleTextView.text = itemVideo.subTitle
            Glide.with(itemVideoBinding.thumbnailImageView.context).load(itemVideo.thumbnail)
                .into(itemVideoBinding.thumbnailImageView)

            itemVideoBinding.root.setOnClickListener {
                callback(itemVideo.url, itemVideo.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        return VideoItemViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem) =
                oldItem == newItem
        }
    }
}
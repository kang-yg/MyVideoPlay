package com.kyg.myvideoplayer.model

import com.google.gson.annotations.SerializedName

data class VideoItemResponse(
    @SerializedName("videos") val data: List<VideoItem>? = null,
    val status: String? = ""
)
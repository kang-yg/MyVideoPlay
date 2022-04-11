package com.kyg.myvideoplayer.model

import com.google.gson.annotations.SerializedName

data class VideoItem(
    val title: String,
    val subTitle: String,
    @SerializedName("thumb") val thumbnail: String,
    @SerializedName("source") val url: String,
)
package com.kyg.myvideoplayer.model.api

import com.kyg.myvideoplayer.model.VideoItem
import com.kyg.myvideoplayer.model.VideoItemResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getVideos(): Response<VideoItemResponse>
}
package com.kyg.myvideoplayer.model.api

import com.kyg.myvideoplayer.model.VideoItem
import com.kyg.myvideoplayer.model.VideoItemResponse
import retrofit2.Response
import retrofit2.http.GET

// Mocky address
// https://run.mocky.io/v3/bddef261-a0e2-4476-8e13-de4197abe910

interface ApiService {
    @GET("/v3/f4c9de78-e344-48e4-a10e-a9711b75aad8")
    suspend fun getVideos(): Response<VideoItemResponse>
}
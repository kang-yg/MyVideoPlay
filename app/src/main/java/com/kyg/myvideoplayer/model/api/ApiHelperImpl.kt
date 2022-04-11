package com.kyg.myvideoplayer.model.api

import com.kyg.myvideoplayer.model.VideoItem
import com.kyg.myvideoplayer.model.VideoItemResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getVideos(): Response<VideoItemResponse> = apiService.getVideos()
}
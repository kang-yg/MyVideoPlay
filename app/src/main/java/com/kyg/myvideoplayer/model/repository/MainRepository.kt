package com.kyg.myvideoplayer.model.repository

import com.kyg.myvideoplayer.model.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getVideos() = apiHelper.getVideos()
}
package com.kyg.myvideoplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyg.myvideoplayer.model.Resource
import com.kyg.myvideoplayer.model.VideoItemResponse
import com.kyg.myvideoplayer.model.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _res = MutableLiveData<Resource<VideoItemResponse>>()

    val res: LiveData<Resource<VideoItemResponse>>
        get() = _res

    private fun getVideos() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        mainRepository.getVideos().let {
            if (it.isSuccessful) {
                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

    init {
        getVideos()
    }
}
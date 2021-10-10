package com.yolo.yolo_android.ui.home_detail

import android.view.View
import androidx.lifecycle.*
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.db.dao.BookMarkDao
import com.yolo.yolo_android.db.entity.MyBookMark
import com.yolo.yolo_android.model.HomeDetailResult
import com.yolo.yolo_android.repository.YoloRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeDetailViewModel @AssistedInject constructor(
    private val yoloRepository: YoloRepository,
    private val bookMarkDao: BookMarkDao,
    @Assisted private val contentId: Int,
    @Assisted private val contentTypeId: Int?
): BaseViewModel() {

    @AssistedFactory
    interface HomeDetailViewModelFactory {
        fun create(contentId: Int, contentTypeId: Int?): HomeDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: HomeDetailViewModelFactory,
            contentId: Int,
            contentTypeId: Int?,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(contentId, contentTypeId) as T
            }
        }
    }

    private val _detailInfo = MutableLiveData<HomeDetailResult>()
    val detailInfo: LiveData<HomeDetailResult> = _detailInfo

    private val _eventResponse = MutableSharedFlow<Boolean>()
    val eventResponse = _eventResponse.asSharedFlow()

    private val _isBookMark = MutableLiveData<Boolean>(false)
    val isBookMark: LiveData<Boolean> = _isBookMark

    init {

        viewModelScope.launch {
            yoloRepository.getTripDetail(
                contentId = contentId,
                contentTypeId = contentTypeId ?: 0,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { result ->
                when(result) {
                    is ResultData.Success -> {
                        result.data.resultCode.let {
                            if (it == 200) {
                                _detailInfo.value = result.data.result
                                _eventResponse.emit(true)
                            }
                        }
                    }
                    else -> {
                        parseError(result)
                    }
                }
            }
            withContext(Dispatchers.IO) {
                _isBookMark.postValue(bookMarkDao.isBookMark(contentId))
            }
        }

    }


    fun onClickBookMark(view: View) {
        _detailInfo.value?.let { data ->
            if (view.isSelected) {
                CoroutineScope(Dispatchers.IO).launch {
                    bookMarkDao.deleteBookMarkById(data.contentId)
                    _isBookMark.postValue(false)
                    _toastMessage.emit("찜 해제 되었습니다.")
                }
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    val myBookMark =MyBookMark(
                        data.contentId,
                        data.contentTypeId,
                        data.title ?: "",
                        data.overview ?: "",
                        data.imageUrl.firstOrNull() ?: "",
                        Calendar.getInstance(Locale.KOREA).time)
                    bookMarkDao.insertBookMark(myBookMark)
                    _isBookMark.postValue(true)
                    _toastMessage.emit("찜 목록에 추가되었습니다.")
                }
            }
        }
    }


}
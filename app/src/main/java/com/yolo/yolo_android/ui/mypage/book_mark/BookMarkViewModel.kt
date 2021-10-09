package com.yolo.yolo_android.ui.mypage.book_mark

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.db.dao.BookMarkDao
import com.yolo.yolo_android.db.entity.MyBookMark
import com.yolo.yolo_android.safeNavigate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val bookMarkDao: BookMarkDao
): BaseViewModel() {

    private val _listData = MutableLiveData<List<MyBookMark>>()
    val listData: LiveData<List<MyBookMark>> = _listData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookMarkDao.getMyBookMark().collect {
                    _listData.postValue(it)
                }
            }
        }
    }
    fun presentDetail(view: View, contentId: Int, contentTypeId: Int) {
        val action = BookMarkFragmentDirections.actionBookMarkFragmentToHomeDetailFragment(contentId, contentTypeId)
        view.findNavController().safeNavigate(action)
    }
}
package com.yolo.yolo_android.ui.community_comment

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.CommentListResponse
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.PostCommentResponse
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.ImageUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class CommentListViewModel @AssistedInject constructor(
    @Assisted private val postId: Int,
    private val yoloRepository: YoloRepository
): BaseViewModel() {

    @AssistedFactory
    interface CommentyListViewModelFactory {
        fun create(postId: Int): CommentListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CommentyListViewModelFactory,
            postId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(postId) as T
            }
        }
    }

    private val _commentList = MutableLiveData<ResultData<CommentListResponse>>()
    val commentList: LiveData<ResultData<CommentListResponse>> = _commentList

    val commentStr = MutableLiveData<String>("")

    val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            commentStr.value = p0.toString()
        }

        override fun afterTextChanged(p0: Editable?) = Unit
    }

    private val _uri = MutableLiveData<Uri?>()
    val uri: LiveData<Uri?> = _uri

    init {
        viewModelScope.launch {
            yoloRepository.getCommentList(
                postId = postId,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { _commentList.value = it }
        }
    }

    fun postComment(context: Context, comment: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, RequestBody>()
            params["content"] = comment.toRequestBody(MultipartBody.FORM)

            var img: MultipartBody.Part? = null
            uri.value?.let {
                val resizedBmp = ImageUtil.getResizeBitmapFromUri(context, it) ?: return@let
                val file = ImageUtil.convertBitmapToFile(context, resizedBmp)
                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                img = MultipartBody.Part.createFormData("image", file.name, reqFile)
                Log.d("aaa", "body= ${img!!.body.contentLength()}")
            }
            yoloRepository.postComment(
                postId = postId,
                param = params,
                image = img,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { parsePostResult(it) }

            _uri.value = null
        }
    }

    private fun parsePostResult(result: ResultData<PostCommentResponse>) {
        when(result) {
            is ResultData.Success -> {
                if (result.data.resultCode == 200) {
                    val listResult = _commentList.value
                    if (listResult is ResultData.Success) {
                        val list = listResult.data.result.toMutableList()
                        result.data.result.author = true
                        list.add(0, result.data.result)
                        listResult.data.result = list
                        _commentList.value = listResult!!
                        commentStr.value = ""

                    }
                }
            }
            else -> {
                parseError(result)
            }
        }
    }

    fun deleteComment(commentId: Int) {
        Log.d("aaa", "deleteComment = $commentId")
        viewModelScope.launch {
            yoloRepository.deleteComment(
                commentId = commentId,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { parseDeleteResult(it, commentId) }
        }
    }

    private fun parseDeleteResult(result: ResultData<CommonResponse>, commentId: Int) {
        when(result) {
            is ResultData.Success -> {
                if (result.data.resultCode == 200) {
                    val listResult = _commentList.value
                    if (listResult is ResultData.Success) {
                        val list = listResult.data.result.toMutableList()
                        listResult.data.result = list.filter { it.commentId !=  commentId }
                        _commentList.value = listResult!!
                    }
                }
            }
            else -> {
                parseError(result)
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        _uri.value = uri
    }

}
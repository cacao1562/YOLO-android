package com.yolo.yolo_android.ui.community_upload

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.utils.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@HiltViewModel
class CommunityUploadViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
): BaseViewModel() {

    /**
     * - 선택한 이미
     */
    private var _uriData = MutableLiveData<List<Uri>>(emptyList())
    val uriData: LiveData<List<Uri>> = _uriData

    /**
     * - 선택한 위치 정보
     */
    private var _document = MutableLiveData<Document>(Document())
    val document: LiveData<Document> = _document

    /**
     * - 입력 내용
     */
    var content = MutableLiveData<String>("")

    /**
     * - 업로드 결과 Response
     */
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    /**
     * - 위치 검색 이동
     */
    val action = CommunityUploadFragmentDirections.actionCommunityUploadFragmentToPlaceListFragment()


    private val uploadIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    private val uploadFlow = uploadIndex.filter { it != 0 }.flatMapLatest { idx ->
        yoloRepository.uploadPost(
            images = images,
            params = params,
            onStart = { _isLoading.postValue(true) },
            onComplete = { _isLoading.postValue(false) }
        )
    }

    private val images = mutableListOf<MultipartBody.Part>()
    private val params = hashMapOf<String, RequestBody>()


    init {
        viewModelScope.launch {
            uploadFlow.collect { result ->
                Log.d("aaa", "usersListFlow=$result")
                when(result) {
                    is ResultData.Success -> {
                        result.data.resultCode?.let {
                            if (it == 200) {
                                _events.emit(it.toString())
                            }
                        }
                    }
                    is ResultData.ErrorMsg -> {
                        _toastMessage.value = result.msg
                    }
                    is ResultData.Error -> {
                        _toastMessage.value = result.errorEntity.message
                    }
                }

            }
        }
    }


    fun setUri(data: List<Uri>) {
        _uriData.value = data.take(3)
    }

    fun setDocument(data: Document) {
        _document.value = data
    }


    fun clickUploadPost(context: Context) {
        if (uriData.value!!.isEmpty() or (content.value!!.isEmpty())) {
            Toast.makeText(context, "이미지, 텍스트 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        images.clear()
        params.clear()
        uriData.value!!.forEach {
            val resizedBmp = ImageUtil.getResizeBitmapFromUri(context, it) ?: return
            val file = ImageUtil.convertBitmapToFile(context, resizedBmp)
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("images", file.name, reqFile)
            Log.d("aaa", "body= ${body.body.contentLength()}")
            images.add(body)
        }
        params["content"] = content.value!!.toRequestBody(MultipartBody.FORM)
        document.value?.let {
            if (it.x.isNotEmpty() and it.y.isNotEmpty()) {
                params["latitude"] = it.y.toRequestBody(MultipartBody.FORM)
                params["longitude"] = it.x.toRequestBody(MultipartBody.FORM)
            }
        }
        uploadIndex.value++

    }

}
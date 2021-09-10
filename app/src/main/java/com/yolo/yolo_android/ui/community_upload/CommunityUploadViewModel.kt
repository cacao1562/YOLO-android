package com.yolo.yolo_android.ui.community_upload

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.repository.ApiRepository
import com.yolo.yolo_android.utils.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
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
    private val apiRepository: ApiRepository
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
        apiRepository.uploadPost(
            images = images,
            params = params,
            onStart = { _isLoading.postValue(true) },
            onComplete = { _isLoading.postValue(false) },
            onError = { _toastMessage.postValue(it) }
        )
    }

    private val images = mutableListOf<MultipartBody.Part>()
    private val params = hashMapOf<String, RequestBody>()


    init {
        viewModelScope.launch {
            uploadFlow.collect {
                Log.d("aaa", "usersListFlow=$it")
                if (it == "success") {
                    _events.emit(it)
                    _toastMessage.value = "업로드 성공"
                }else _toastMessage.value = "업로드 실패.."
            }
        }
    }

    /**
     * - 이미지 선택 라이브러리 열기
     * (최대 3장)
     */
    fun clickPresentImagePicker(view: View) {

        TedImagePicker.with(view.context).startMultiImage {
            if (it.size > 3) {
                val bundle = bundleOf(
                            "title" to view.context.getString(R.string.alert),
                            "msg" to view.context.getString(R.string.alert_msg_picture_limit))
                view.findNavController().navigate(R.id.commonDialog, bundle)
            }
            _uriData.value = it.take(3)
        }
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
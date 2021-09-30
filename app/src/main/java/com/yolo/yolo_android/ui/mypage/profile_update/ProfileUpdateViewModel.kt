package com.yolo.yolo_android.ui.mypage.profile_update

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.CommonResponse
import com.yolo.yolo_android.model.MyProfile
import com.yolo.yolo_android.repository.YoloRepository
import com.yolo.yolo_android.ui.dialog.CommonDialog
import com.yolo.yolo_android.utils.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
    private val yoloRepository: YoloRepository
): BaseViewModel() {

    private val _profile = MutableLiveData<MyProfile>()
    val profile: LiveData<MyProfile> = _profile

    val inputNickname = MutableLiveData<String>("")

    private val _isEnable = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnable

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

    private val _updateSuccess = MutableSharedFlow<String>()
    val updateSuccess = _updateSuccess.asSharedFlow()

    val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val str = p0.toString()
            _isEnable.value = str.isNotEmpty()
            inputNickname.value = str
        }

        override fun afterTextChanged(p0: Editable?) = Unit
    }

    fun setProfile(myProfile: MyProfile) {
        _profile.value = myProfile
        inputNickname.value = myProfile.nickname
    }

    fun clearInput() {
        inputNickname.value = ""
    }

    fun presentImagePicker(context: Context) {
        TedImagePicker.with(context).start {
            _imageUri.value = it
            _isEnable.value = true
        }
    }


    fun updateProfile(context: Context) {
        if (inputNickname.value!!.isEmpty()) {
            viewModelScope.launch {
                _toastMessage.emit("닉네임을 입력해주세요.")
            }
            return
        }
        viewModelScope.launch {
            val params = hashMapOf<String, RequestBody>()
            params["nickname"] = inputNickname.value!!.toRequestBody(MultipartBody.FORM)

            var img: MultipartBody.Part? = null
            _imageUri.value?.let {
                val resizedBmp = ImageUtil.getResizeBitmapFromUri(context, it) ?: return@let
                val file = ImageUtil.convertBitmapToFile(context, resizedBmp)
                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                img = MultipartBody.Part.createFormData("image", file.name, reqFile)
                Log.d("aaa", "body= ${img!!.body.contentLength()}")
            }
            yoloRepository.updateProfile(
                param = params,
                image = img,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { parseProfileResult(it, "update") }

        }
    }

    fun deleteProfileImage() {
        if (_profile.value?.imageUrl.isNullOrEmpty()) {
            viewModelScope.launch {
                _toastMessage.emit("이미지 정보가 없습니다.")
            }
            return
        }
        viewModelScope.launch {
            yoloRepository.deleteProfileImage(
                imageUrl = _profile.value?.imageUrl!!,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) }
            ).collect { parseProfileResult(it, "delete") }
        }
    }

    private fun parseProfileResult(result: ResultData<CommonResponse>, apiType: String) {
        when(result) {
            is ResultData.Success -> {
                if (result.data.resultCode == 200) {
                    viewModelScope.launch {
                        _updateSuccess.emit(apiType)
                    }
                }
            }
            else -> {
                parseError(result)
            }
        }
    }
}
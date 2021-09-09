package com.yolo.yolo_android.ui.community_upload

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yolo.yolo_android.R
import com.yolo.yolo_android.asMultipart
import com.yolo.yolo_android.base.BaseViewModel
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.repository.ApiRepository
import com.yolo.yolo_android.ui.dialog.CommonDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import com.yolo.yolo_android.uri2path
import java.io.IOException
import java.lang.Exception


@HiltViewModel
class CommunityUploadViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): BaseViewModel() {

    private var _uriData = MutableLiveData<List<Uri>>(emptyList())
    val uriData: LiveData<List<Uri>> = _uriData

    private var _document = MutableLiveData<Document>(Document())
    val document: LiveData<Document> = _document

    var description = MutableLiveData<String>("")

    var uploadResponse = MutableLiveData<String>()

    val action = CommunityUploadFragmentDirections.actionCommunityUploadFragmentToPlaceListFragment()

    fun clickPresentImagePicker(context: Context) {
        TedImagePicker.with(context).startMultiImage {
            if (it.size > 3) {
                CommonDialog
                    .newInstance(context.getString(R.string.alert), context.getString(R.string.alert_msg_picture_limit))
                    .show((context as FragmentActivity).supportFragmentManager, CommonDialog::class.java.simpleName)
            }
            _uriData.value = it.take(3)
        }
    }

    fun setDocument(data: Document) {
        _document.value = data
    }

    private val uploadIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    val images = mutableListOf<MultipartBody.Part>()
    val params = hashMapOf<String, RequestBody>("content" to description.value!!.toRequestBody(MultipartBody.FORM))

    private val usersListFlow = uploadIndex.flatMapLatest { page ->
        Log.d("aaa", "uploadIndex=$page")
        apiRepository.uploadPost(
            images = images,
            params = params,
            onStart = { _isLoading.postValue(true) },
            onComplete = { _isLoading.postValue(false) },
            onError = { _toastMessage.postValue(it) }
        )
    }

    init {

        viewModelScope.launch {
            usersListFlow.collect {
                Log.d("aaa", "usersListFlow=$it")
//                uploadResponse.value = it
            }
        }
    }

    val mBitmap = MutableLiveData<Bitmap>()
    fun clickUploadPost(context: Context) {
        if (uriData.value!!.isEmpty() or (description.value!!.isEmpty())) {
            Toast.makeText(context, "이미지, 텍스트 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
        }
//        val images = mutableListOf<MultipartBody.Part>()
        uriData.value!!.forEach {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }

                val copyB = bitmap.copy(Bitmap.Config.ARGB_8888, true)

                val options = BitmapFactory.Options()
                options.inSampleSize = 4

//                val path = uri2path(context, it)
//                if (path == null) return
//                val bitmap = BitmapFactory.decodeFile(path, options)
                var bmpWidth = bitmap.width.toFloat()
                var bmpHeight = bitmap.height.toFloat()
                Log.d("aaa", "bmpWidth= ${bmpWidth}")
                Log.d("aaa", "bmpHeight= ${bmpHeight}")
                Log.d("aaa", "bitmap byteCount= ${bitmap.byteCount}")
                Log.d("aaa", "bitmap rowBytes= ${bitmap.rowBytes}")
                if (bmpWidth > 1080) {
                    // 원하는 너비보다 클 경우의 설정
                    val mWidth: Float = bmpWidth / 100
                    val scale: Float = 1080 / mWidth
                    bmpWidth *= scale / 100
                    bmpHeight *= scale / 100
                } else if (bmpHeight > 1080) {
                    // 원하는 높이보다 클 경우의 설정
                    val mHeight: Float = bmpHeight / 100
                    val scale: Float = 1080 / mHeight
                    bmpWidth *= scale / 100
                    bmpHeight *= scale / 100
                }
                Log.d("aaa", "scale bmpWidth= ${bmpWidth}")
                Log.d("aaa", "sacle bmpHeight= ${bmpHeight}")
                val resizedBmp =
                    Bitmap.createScaledBitmap(bitmap, bmpWidth.toInt(), bmpHeight.toInt(), true)

                Log.d("aaa", "resize bmpWidth= ${resizedBmp.width}")
                Log.d("aaa", "resize bmpHeight= ${resizedBmp.height}")
                Log.d("aaa", "resize byteCount= ${resizedBmp.byteCount}")
                Log.d("aaa", "resize rowBytes= ${resizedBmp.rowBytes}")

                mBitmap.value = resizedBmp
                val body = it.asMultipart("images", context.contentResolver)
                Log.d("aaa", "body= ${body?.body?.contentLength()}")

                body?.let { images.add(body) }


            }catch (e: IOException) {
                e.printStackTrace()
            }



        }
//        val params = hashMapOf<String, RequestBody>("content" to description.value!!.toRequestBody(MultipartBody.FORM))
        document.value?.let {
            if (it.x.isNotEmpty() and it.y.isNotEmpty()) {
                params["latitude"] = it.y.toRequestBody(MultipartBody.FORM)
                params["longitude"] = it.x.toRequestBody(MultipartBody.FORM)
            }
        }
//        uploadIndex.value++

    }

}
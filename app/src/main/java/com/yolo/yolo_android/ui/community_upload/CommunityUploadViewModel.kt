package com.yolo.yolo_android.ui.community_upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yolo.yolo_android.model.Document
import gun0912.tedimagepicker.builder.TedImagePicker

class CommunityUploadViewModel: ViewModel() {

    private var _uriData = MutableLiveData<List<Uri>>(emptyList())
    val uriData: LiveData<List<Uri>> = _uriData

    private var _document = MutableLiveData<Document>(Document())
    val document: LiveData<Document> = _document

    val action = CommunityUploadFragmentDirections.actionCommunityUploadFragmentToPlaceListFragment()

    fun clickPresentImagePicker(context: Context) {
        TedImagePicker.with(context).startMultiImage {
            _uriData.value = it
        }
    }

    fun setDocument(data: Document) {
        _document.value = data
    }

}
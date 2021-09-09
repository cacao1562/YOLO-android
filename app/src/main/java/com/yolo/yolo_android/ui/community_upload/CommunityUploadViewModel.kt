package com.yolo.yolo_android.ui.community_upload

import android.content.Context
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yolo.yolo_android.R
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.ui.dialog.CommonDialog
import gun0912.tedimagepicker.builder.TedImagePicker

class CommunityUploadViewModel: ViewModel() {

    private var _uriData = MutableLiveData<List<Uri>>(emptyList())
    val uriData: LiveData<List<Uri>> = _uriData

    private var _document = MutableLiveData<Document>(Document())
    val document: LiveData<Document> = _document

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

}
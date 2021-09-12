package com.yolo.yolo_android.common.extensions

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object ToastExt {
    fun Fragment.toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    fun AppCompatActivity.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
package com.yolo.yolo_android

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import java.io.File

fun View.delayOnLifecycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMillis)
        block()
    }
}

fun TabLayout.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    for (i in 0 until this.tabCount) {
        val tab = (this.getChildAt(0) as ViewGroup).getChildAt(i)
        val p = tab.layoutParams as ViewGroup.MarginLayoutParams
        if (i == 0) p.setMargins(right, top, right, bottom)
        else p.setMargins(left, top, right, bottom)
        tab.requestLayout()
    }
}

fun Int.dpToPx(): Int = (this * YoLoApplication.context!!.resources.displayMetrics.density).toInt()


fun uri2path(context: Context, contentUri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = context.contentResolver.query(contentUri, proj, null, null, null)
    cursor?.moveToNext()
    val path = cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
    val uri = Uri.fromFile(File(path))
    cursor?.close()
    return path
}


const val REQUEST_PERMISSIONS = 1212
fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
}

fun requestPermission(activity: Activity, permission: String) {
    ActivityCompat.requestPermissions(activity, listOf(permission).toTypedArray(), REQUEST_PERMISSIONS)
}
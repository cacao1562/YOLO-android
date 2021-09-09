package com.yolo.yolo_android

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
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

fun Activity.showSystemUI() {
    window?.statusBarColor = Color.TRANSPARENT

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // show app content in fullscreen, i. e. behind the bars when they are shown (alternative to
        // deprecated View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.setDecorFitsSystemWindows(false)
        // finally, show the system bars
        window.insetsController?.show(WindowInsets.Type.systemBars())
    } else {
        // Shows the system bars by removing all the flags
        // except for the ones that make the content appear under the system bars.
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

fun Activity.hideSystemUI() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)

        window.insetsController?.let {
            // Default behavior is that if navigation bar is hidden, the system will "steal" touches
            // and show it again upon user's touch. We just want the user to be able to show the
            // navigation bar by swipe, touches are handled by custom code -> change system bar behavior.
            // Alternative to deprecated SYSTEM_UI_FLAG_IMMERSIVE.
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // make navigation bar translucent (alternative to deprecated
            // WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // - do this already in hideSystemUI() so that the bar
            // is translucent if user swipes it up
            window.navigationBarColor = getColor(android.R.color.transparent)
            // Finally, hide the system bars, alternative to View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            // and SYSTEM_UI_FLAG_FULLSCREEN.
            it.hide(WindowInsets.Type.systemBars())
        }
    } else {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                // Do not let system steal touches for showing the navigation bar
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Keep the app content behind the bars even if user swipes them up
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        // make navbar translucent - do this already in hideSystemUI() so that the bar
        // is translucent if user swipes it up
        @Suppress("DEPRECATION")
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

}

fun Uri.asMultipart(name: String, contentResolver: ContentResolver): MultipartBody.Part? {
    return contentResolver.query(this, null, null, null, null)?.let {
        if (it.moveToNext()) {
            val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? {
                    return contentResolver.getType(this@asMultipart)?.toMediaType()
                }

                override fun writeTo(sink: BufferedSink) {
                    sink.writeAll(contentResolver.openInputStream(this@asMultipart)?.source()!!)
                }
            }
            it.close()
            MultipartBody.Part.createFormData(name, displayName, requestBody)
        } else {
            it.close()
            null
        }
    }
}
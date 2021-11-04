package com.yolo.yolo_android


import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDirections
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
        if (i == 0) p.setMargins(24.dpToPx(), top, right, bottom)
        else p.setMargins(left, top, right, bottom)
        tab.requestLayout()
    }
}

fun Int.dpToPx(): Int = (this * YoLoApplication.context!!.resources.displayMetrics.density).toInt()

fun Int.toDp() : Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), YoLoApplication.context!!.resources.displayMetrics)

fun uri2path(context: Context, contentUri: Uri): Pair<String?, Long?> {
    if (contentUri.path!!.startsWith("/storage")) {
        return Pair(contentUri.path, File(contentUri.path).length())
    }

    val proj = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE)
    val cursor: Cursor? = context.contentResolver.query(contentUri, proj, null, null, null)
    cursor?.moveToNext()
    val path = cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
    val size = cursor?.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE))
    cursor?.close()
    return Pair(path, size)
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

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun Group.addOnClickListener(listener: (view: View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

fun rotateFilterArrow(isDefault: Boolean, view: View) {
    val anim = if (isDefault) RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    else RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    anim.duration = 250
    anim.interpolator = LinearInterpolator()
    anim.fillAfter = true
    view.startAnimation(anim)
}

fun String.fromHtmlStr(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}

fun setUnderLineText(textView: TextView) {
    val content = SpannableString(textView.text.toString())
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    textView.text = content
}


fun getWindowHeight(context: Context): Int {
    // Calculate window height for fullscreen use
    val displayMetrics = DisplayMetrics()
    (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun presentAppSetting(context: Context) {
    Intent().also { intent ->
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:${context.packageName}")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
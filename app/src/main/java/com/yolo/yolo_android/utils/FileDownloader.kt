package com.yolo.yolo_android.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.net.URLEncoder

class FileDownloader(
    private val context: Context,
    private val fullPath: String,
    private val fileName: String
) {
    fun download() {
        val uri: Uri = Uri.parse(fullPath)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try {
            val request = DownloadManager.Request(uri)
            request.setTitle(fileName)
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val downloadId = downloadManager.enqueue(request)
        } catch (e: Exception) {
            MyLogger.e("file download error : ${e.message}")
            e.printStackTrace()
        }
    }
}
package com.yolo.yolo_android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.yolo.yolo_android.uri2path
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object ImageUtil {

    private val fixSize = 1080F

    fun getResizeBitmapFromUri(context: Context, uri: Uri): Bitmap? {

        try {
            val img = uri2path(context, uri)
            val path = img.first ?: return null
            if (img.second != null) {
                // 파일 사이즈 1MB 이상이면 resize
                if (img.second!! > 1048576) {
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 2
                    var bitmap = BitmapFactory.decodeFile(path, options)
                    val degrees = getOrientationOfImage(path)
                    if (degrees != -1) bitmap = getRotatedBitmap(bitmap, degrees) ?: return null
                    var bmpWidth = bitmap.width.toFloat()
                    var bmpHeight = bitmap.height.toFloat()
                    Log.d("aaa", "bmpWidth= ${bmpWidth}")
                    Log.d("aaa", "bmpHeight= ${bmpHeight}")

                    if (bmpWidth > fixSize || bmpHeight > fixSize) {
                        val hRatio = fixSize / bmpHeight
                        val wRatio = fixSize / bmpWidth
                        val ratio = if (bmpHeight > bmpWidth) hRatio else wRatio
                        bmpWidth *= ratio
                        bmpHeight *= ratio
                    }

                    Log.d("aaa", "scale bmpWidth= ${bmpWidth}")
                    Log.d("aaa", "sacle bmpHeight= ${bmpHeight}")

                    return Bitmap.createScaledBitmap(bitmap, bmpWidth.toInt(), bmpHeight.toInt(), true)
                }else {
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 1
                    return BitmapFactory.decodeFile(path, options)
                }
            }

            return null

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun getOrientationOfImage(filepath: String): Int {
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(filepath)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    @Throws(Exception::class)
    fun getRotatedBitmap(bitmap: Bitmap?, degrees: Int): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0) return bitmap
        val m = Matrix()
        m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    fun convertBitmapToFile(context: Context, bitmap: Bitmap): File {

        val fileName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss_SSS"))
            "$name.jpg"
        }else {
            SimpleDateFormat("yyyy-MM-dd_HHmmss_SSS").format(Date()) + ".jpg"
        }
        //create a file to write bitmap data
        val file = File(context.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}
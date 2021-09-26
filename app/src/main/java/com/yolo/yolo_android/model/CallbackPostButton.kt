package com.yolo.yolo_android.model

import android.view.View

sealed class CallbackPostButton {
    data class More(val postId: Int) : CallbackPostButton()
    data class Delete(val postId: Int) : CallbackPostButton()
    data class Like(val view: View, val postId: Int, val likeCount: Int, val isLike: Boolean) : CallbackPostButton()
    data class Map(val latitude: Double, val longitude: Double) : CallbackPostButton()
    data class Comment(val postId: Int) : CallbackPostButton()
}
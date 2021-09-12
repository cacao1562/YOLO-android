package com.yolo.yolo_android.ui.custom

import android.animation.ValueAnimator
import android.graphics.Rect
import com.github.ybq.android.spinkit.animation.SpriteAnimatorBuilder
import com.github.ybq.android.spinkit.sprite.CircleSprite
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.sprite.SpriteContainer

class MyThreeBounce : SpriteContainer() {

    override fun onCreateChild(): Array<Sprite> {
        return arrayOf(
            Bounce(),
            Bounce(),
            Bounce()
        )
    }

    override fun onChildCreated(vararg sprites: Sprite) {
        super.onChildCreated(*sprites)
        sprites[1].animationDelay = 120
        sprites[2].animationDelay = 240
    }

    override fun onBoundsChange(rect: Rect) {
        var bounds = rect
        super.onBoundsChange(bounds)
        bounds = clipSquare(bounds)
        val radius = bounds.width() / 8
        val top = bounds.centerY() - radius
        val bottom = bounds.centerY() + radius
        for (i in 0 until childCount) {
            val left = (bounds.width() * i / 3 + (radius / 2))
            getChildAt(i).setDrawBounds(
                left, top, left + radius * 2, bottom
            )
        }
    }

    private class Bounce : CircleSprite() {
        override fun onCreateAnimation(): ValueAnimator {
            val fractions = floatArrayOf(0f, 0.4f, 0.8f, 1f)
            return SpriteAnimatorBuilder(this)
                .scale(fractions, 0.4f, 1f, 0.8f, 0.4f)
                .duration(800)
                .easeInOut(*fractions)
                .build()
        }

        init {
            scale = 0.4f
        }
    }

}
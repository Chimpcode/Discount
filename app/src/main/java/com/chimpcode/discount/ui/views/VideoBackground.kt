package com.chimpcode.discount.ui.views

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView

/**
 * Created by anargu on 2/5/18.
 */
class VideoBackground : VideoView {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            val mpField = VideoView::class.java.getDeclaredField("mMediaPlayer")
            mpField.isAccessible = true
            val mediaPlayer: MediaPlayer = mpField.get(this) as MediaPlayer

            val width = View.getDefaultSize(mediaPlayer.videoWidth, widthMeasureSpec)
            val height = View.getDefaultSize(mediaPlayer.videoHeight, heightMeasureSpec)
            setMeasuredDimension(width, height)
        } catch (e : Exception) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
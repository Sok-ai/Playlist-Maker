package com.example.playlistmaker.core.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.use
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val rect = RectF()

    // region Images
    private var imageStart: Bitmap? = null
    private var imagePause: Bitmap? = null

    // endregion

    // region Sizes
    private val defaultSizePx = resources.getDimensionPixelSize(R.dimen.playback_button_size)


    // endregion

    // region States
    private var isPlaying = false

    // endregion

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).use {
            imageStart = it.getDrawable(R.styleable.PlaybackButtonView_iconStart)?.toBitmap()
            imagePause = it.getDrawable(R.styleable.PlaybackButtonView_iconPause)?.toBitmap()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val contentWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> defaultSizePx
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(widthSize, defaultSizePx)
            else -> error("Unknown mode")
        }
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val contentHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> defaultSizePx
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(heightSize, defaultSizePx)
            else -> error("Unknown mode")
        }

        val size = min(contentWidth, contentHeight)
        setMeasuredDimension(size, size)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (!isPlaying) {
            imageStart?.let {
                canvas.drawBitmap(it, null, rect, null)
            }
        } else {
            imagePause?.let {
                canvas.drawBitmap(it, null, rect, null)
            }
        }
    }

    fun setPlayingState(isPlaying: Boolean) {
        if (this.isPlaying == isPlaying) return
        this.isPlaying = isPlaying
        invalidate()
    }
}
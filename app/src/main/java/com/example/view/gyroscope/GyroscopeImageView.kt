package com.example.view.gyroscope

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.util.AttributeSet
import android.graphics.Canvas
import kotlin.math.abs

class GyroscopeImageView : AppCompatImageView {

    private var mScaleX = 0.0
    private var mScaleY = 0.0
    private var mLenX = 0f
    private var mLenY = 0f
    var mAngleX = 0.0
    var mAngleY = 0.0
    var offsetX = 0f
    var offsetY = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        scaleType = ScaleType.CENTER
    }

    override fun setScaleType(scaleType: ScaleType) {
        super.setScaleType(ScaleType.CENTER)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        GyroscopeManager.addView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        GyroscopeManager.removeView(this)
    }

    fun update(scaleX: Double, scaleY: Double) {
        mScaleX = scaleX
        mScaleY = scaleY
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        if (drawable != null) {
            val drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight
            mLenX = abs((drawableWidth - width) * 0.5f)
            mLenY = abs((drawableHeight - height) * 0.5f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null || isInEditMode) {
            super.onDraw(canvas)
            return
        }
        offsetX = (mLenX * mScaleX).toFloat()
        offsetY = (mLenY * mScaleY).toFloat()
        canvas.save()
        canvas.translate(offsetX, offsetY)
        super.onDraw(canvas)
        canvas.restore()
    }

}
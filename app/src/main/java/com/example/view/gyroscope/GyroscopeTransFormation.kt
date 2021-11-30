package com.example.view.gyroscope

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import android.graphics.Bitmap
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

/**
 * 根据控件的宽高来拉伸支持陀螺仪滚动图片的Transformation
 * 参考：https://guolin.blog.csdn.net/article/details/71524668
 */
class GyroscopeTransFormation(
    // 控件宽度
    private val mWidgetWidth: Int,
    // 控件高度
    private val mWidgetHeight: Int
) : BitmapTransformation() {

    // 目标宽度
    private var mTargetWidth = 0.0

    // 目标高度
    private var mTargetHeight = 0.0

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        if (toTransform.width == 0 || toTransform.height == 0) {
            return toTransform
        }
        val source = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        mTargetWidth = source.width.toDouble()
        mTargetHeight = source.height.toDouble()
        // 图片的宽高比
        val ratio = mTargetWidth / mTargetHeight
        // 图片缩放后与控件边的距离
        val distance: Int
        if (mWidgetHeight <= mWidgetWidth) {
            distance = mWidgetHeight / 8
            mTargetWidth = (mWidgetWidth + 2 * distance).toDouble()
            mTargetHeight = mTargetWidth / ratio
        } else {
            distance = mWidgetWidth / 8
            mTargetHeight = (mWidgetHeight + 2 * distance).toDouble()
            mTargetWidth = mTargetHeight * ratio
        }
        val desiredWidth = mTargetWidth.toInt()
        val desiredHeight = mTargetHeight.toInt()
        val result = Bitmap.createScaledBitmap(source, desiredWidth, desiredHeight, false)
        if (result != source) {
            source.recycle()
        }
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}
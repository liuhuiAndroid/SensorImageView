package com.example.view.gyroscope

import android.hardware.SensorEventListener
import java.util.HashMap
import android.app.Activity
import android.content.Context
import java.util.ArrayList
import android.hardware.SensorManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.View

object GyroscopeManager : SensorEventListener {

    // 将纳秒转化为秒
    private const val NS2S = 1.0f / 1000000000.0f

    // 维护 GyroscopeImageView 的 状态, 需要传感器处理的 GyroscopeImageView 其对应的 value 为 true
    private val mViewsMap: MutableMap<GyroscopeImageView, Boolean> = HashMap(10)

    // 维护需要传感器处理的 Activity
    private val mActivityList: MutableList<Activity> = ArrayList()
    private var sensorManager: SensorManager? = null
    private var mLastTimestamp: Long = 0
    private const val mMaxAngle = Math.PI / 2

    fun addView(gyroscopeImageView: GyroscopeImageView) {
        mViewsMap[gyroscopeImageView] =
            mActivityList.contains(getActivityFromView(gyroscopeImageView))
    }

    fun removeView(gyroscopeImageView: GyroscopeImageView) {
        mViewsMap.remove(gyroscopeImageView)
    }

    fun register(activity: Activity) {
        mActivityList.add(activity)
        for (view in mViewsMap.keys) {
            for (a in mActivityList) {
                if (getActivityFromView(view) === a) {
                    mViewsMap[view] = true
                }
            }
        }
        if (sensorManager == null) {
            sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }
        val sensor: Sensor
        sensorManager?.let {
            // 陀螺仪
            sensor = it.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            // SENSOR_DELAY_FASTEST(0微秒);最快。最低延迟，一般不是特别敏感的处理不推荐使用
            // SENSOR_DELAY_GAME(20000微秒);游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别
            // SENSOR_DELAY_NORMAL(200000微秒);普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象
            // SENSOR_DELAY_UI(60000微秒);用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用
            it.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
            mLastTimestamp = 0
        }
    }

    fun unregister(activity: Activity) {
        mActivityList.remove(activity)
        for (view in mViewsMap.keys) {
            if (getActivityFromView(view) === activity) {
                mViewsMap[view] = false
            }
        }
        sensorManager?.unregisterListener(this)
        sensorManager = null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            if (mLastTimestamp != 0L) {
                for ((key, value) in mViewsMap) {
                    if (value) {
                        key.mAngleX += event.values[0] * (event.timestamp - mLastTimestamp) * NS2S * 10.0f
                        key.mAngleY += event.values[1] * (event.timestamp - mLastTimestamp) * NS2S * 10.0f
                        if (key.mAngleX > mMaxAngle) {
                            key.mAngleX = mMaxAngle
                        }
                        if (key.mAngleX < -mMaxAngle) {
                            key.mAngleX = -mMaxAngle
                        }
                        if (key.mAngleY > mMaxAngle) {
                            key.mAngleY = mMaxAngle
                        }
                        if (key.mAngleY < -mMaxAngle) {
                            key.mAngleY = -mMaxAngle
                        }
                        key.update(key.mAngleY / mMaxAngle, key.mAngleX / mMaxAngle)
                    }
                }
            }
            mLastTimestamp = event.timestamp
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun getActivityFromView(view: View): Activity {
        return view.context as Activity
    }

}
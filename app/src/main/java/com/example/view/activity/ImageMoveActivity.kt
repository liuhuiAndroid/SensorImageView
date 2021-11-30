package com.example.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.view.R
import com.example.view.gyroscope.GyroscopeManager
import com.bumptech.glide.request.RequestOptions
import com.example.view.gyroscope.GyroscopeTransFormation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_move.*

/**
 * 参考：https://blog.csdn.net/qq_34744970/article/details/108666082
 * 参考：https://www.freesion.com/article/8132176534/
 * 参考：https://github.com/JY39/GyroscopeImageDemo
 */
class ImageMoveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_move)
        initView()
    }

    override fun onResume() {
        super.onResume()
        GyroscopeManager.register(this)
    }

    override fun onPause() {
        super.onPause()
        GyroscopeManager.unregister(this)
    }

    private fun initView() {
        gyroscopeImageView.post {
            val options = RequestOptions().apply {
                transform(GyroscopeTransFormation(gyroscopeImageView.width, gyroscopeImageView.height))
            }
            Glide.with(this@ImageMoveActivity).load(PIC_URL).apply(options).into(gyroscopeImageView)
        }
//        gyroscopeImageView.setOnClickListener(v -> {
//            CoverActivity.startActivityWithAnimation(ImageMoveActivity.this, PIC_URL,
//                    gyroscopeImageView);
//        });
    }

    companion object {
        private const val PIC_URL =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fgb.cri.cn%2Fmmsource%2Fimages%2F2013%2F10%2F30%2F15%2F15984052342721205435.jpg&refer=http%3A%2F%2Fgb.cri.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1640844084&t=95a0ecda6d337e912297feedb21dd1cc"
    }
}
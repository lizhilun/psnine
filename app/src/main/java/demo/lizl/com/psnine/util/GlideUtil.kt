package demo.lizl.com.psnine.util

import android.widget.ImageView
import demo.lizl.com.psnine.GlideApp

object GlideUtil
{
    /**
     * 加载图片并显示
     */
    fun displayImage(imageView: ImageView, imageUri: String)
    {
        GlideApp.with(imageView.context).load(imageUri).into(imageView)
    }
}
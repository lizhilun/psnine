package demo.lizl.com.psnine.util

import android.content.Context
import android.widget.ImageView
import demo.lizl.com.psnine.GlideApp

object GlideUtil
{
    /**
     * 加载图片并显示
     */
    fun displayImage(context: Context, imageUri: String, imageView: ImageView)
    {
        GlideApp.with(context).load(imageUri).into(imageView)
    }
}
package demo.lizl.com.psnine.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import demo.lizl.com.psnine.GlideApp

object GlideUtil
{

    @JvmStatic
    @BindingAdapter("app:imageUri")
    fun bindImageUrl(imageView: ImageView, imageUri: String?)
    {
        if (imageUri.isNullOrEmpty()) return
        GlideApp.with(imageView.context).load(imageUri).into(imageView)
    }

    /**
     * 加载图片并显示
     */
    fun displayImage(imageView: ImageView, imageUri: String)
    {
        GlideApp.with(imageView.context).load(imageUri).into(imageView)
    }
}
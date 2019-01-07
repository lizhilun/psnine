package demo.lizl.com.psnine.util;

import android.content.Context;
import android.widget.ImageView;

import demo.lizl.com.psnine.GlideApp;

public class GlideUtil
{

    /**
     * 加载图片并显示
     */
    public static void displayImage(Context context, String imageUri, ImageView imageView)
    {
        GlideApp.with(context).load(imageUri).into(imageView);
    }

}

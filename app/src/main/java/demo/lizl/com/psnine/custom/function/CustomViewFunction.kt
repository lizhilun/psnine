package demo.lizl.com.psnine.custom.function

import android.view.View
import androidx.core.content.ContextCompat

fun View.setBackgroundColorRes(colorRes: Int)
{
    setBackgroundColor(ContextCompat.getColor(context, colorRes))
}
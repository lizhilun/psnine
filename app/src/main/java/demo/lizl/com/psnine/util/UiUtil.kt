package demo.lizl.com.psnine.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.InputFilter
import android.text.Spanned


object UiUtil
{
    private val noWrapInputFilter = InputFilter { source: CharSequence, _: Int, _: Int, _: Spanned, _: Int, _: Int ->
        if (source.toString().contentEquals("\n"))
        {
            return@InputFilter ""
        }
        else
        {
            return@InputFilter null
        }
    }

    /**
     * 获取过滤换行的InputFilter
     */
    fun getNoWrapInputFilter() = noWrapInputFilter

    /**
     * 跳转到浏览器
     */
    fun turnToWebBrowser(context: Context, requestUrl: String)
    {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = Uri.parse(requestUrl)
        context.startActivity(intent)
    }
}